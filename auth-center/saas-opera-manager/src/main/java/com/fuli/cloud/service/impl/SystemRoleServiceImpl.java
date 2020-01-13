package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.commons.utils.JacksonUtil;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.commons.utils.QWrapper;
import com.fuli.cloud.commons.utils.RedisService;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.constant.RoleStatusEnum;
import com.fuli.cloud.dto.*;
import com.fuli.cloud.handler.CustomException;
import com.fuli.cloud.mapper.*;
import com.fuli.cloud.model.*;
import com.fuli.cloud.service.MenuService;
import com.fuli.cloud.service.RedisManageService;
import com.fuli.cloud.service.SystemHomepageModuleService;
import com.fuli.cloud.service.SystemRoleService;
import com.fuli.cloud.vo.*;
import com.fuli.cloud.vo.menu.AuthModuleVO;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色管理
 *
 * @author xq
 */
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRoleDO> implements SystemRoleService {

    @Autowired
    SystemRoleMapper mapper;
    @Autowired
    private SystemMenuMapper systemMenuMapper;
    @Autowired
    SystemHomepageModuleMapper systemHomepageModuleMapper;
    @Autowired
    SystemRoleUserMapper systemRoleUserMapper;
    @Resource
    SystemModuleMapper systemModuleMapper;
    @Resource
    MenuService menuService;
    @Resource
    SystemHomepageModuleService systemHomepageModuleService;
    @Autowired
    SystemRoleMenuMapper systemRoleMenuMapper;
    @Autowired
    SystemRoleHomepageModuleMapper systemRoleHomepageModuleMapper;
    @Autowired
    private RedisManageService redisManageService;
    @Autowired
    private RedisService redisService;

    /**
     * 添加或修改菜单权限及首页模块
     *
     * @param menuIds 菜单id集合
     * @param homeIds 模块id集合
     */
    private void addMenuAndHomeModule(Set<Integer> menuIds, Set<Integer> homeIds, Integer roleId, boolean delete) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_id", roleId);
        // 插入权限菜单数据
        if (!CollectionUtils.isEmpty(menuIds)) {
            /**查询菜单中是否包含审批功能  包含的话默认添加角色首页模块*/
            QueryWrapper<SystemMenuDO> menuMenu = new QueryWrapper<>();
            menuMenu.in(!CollectionUtils.isEmpty(menuIds), "id", menuIds);
            menuMenu.like("name", "审批");
            List<SystemMenuDO> menus = systemMenuMapper.selectList(menuMenu);
            QueryWrapper<SystemHomepageModuleDO> homeMenu = new QueryWrapper<>();
            homeMenu.eq("is_default", 0);
            /**当菜单存在审批时过滤*/
            if (menus != null && menus.size() > 0) {
                //根据菜单名称查询是否具有对应审批模块
                List<String> menuNames = new ArrayList<>();
                menus.forEach(systemMenuDO -> {
                    menuNames.add(systemMenuDO.getName());
                });
                homeMenu.or(i -> i.in("name", menuNames));
            }
            List<SystemHomepageModuleDO> homeMenus = systemHomepageModuleMapper.selectList(homeMenu);
            JacksonUtil.dumnToPrettyJsonInfo("添加修改角色查询到的模块：", homeMenus);
            /**添加角色权限默认模块*/
            homeMenus.forEach(homePageIds -> {
                homeIds.add(homePageIds.getId());
            });
            if (delete) {
                systemRoleMenuMapper.delete(queryWrapper);
            }
            menuIds.forEach(
                    id -> {
                        systemRoleMenuMapper.insert(new SystemRoleMenuDO(roleId, id));
                    });
        }
        // 插入首页模块数据
        if (!CollectionUtils.isEmpty(homeIds)) {
            if (delete) {
                systemRoleHomepageModuleMapper.delete(queryWrapper);
            }
            homeIds.forEach(
                    id -> {
                        systemRoleHomepageModuleMapper.insert(new SystemRoleHomepageModuleDO(roleId, id));
                    });
        }
    }

    /**
     * 添加菜单权限及首页模块
     *
     * @param menuIds 菜单id集合
     * @param homeIds 模块id集合
     */
    private void checkMenuAndHomeModuleData(Set<Integer> menuIds, Set<Integer> homeIds) {
        // 验证权限菜单的合法性
        if (!CollectionUtils.isEmpty(menuIds)) {
            List<SystemMenuDO> menus = systemMenuMapper.selectBatchIds(menuIds);
            if (CollectionUtils.isEmpty(menus) || (menus.size() != menuIds.size())) {
                throw new CustomException(CodeEnum.MENU_ID_ERROR);
            }
            checkMenuTree(menus);
        }
        // 验证首页模块id的合法性
        if (!CollectionUtils.isEmpty(homeIds)) {
            List<SystemHomepageModuleDO> list = systemHomepageModuleMapper.selectBatchIds(homeIds);
            if (CollectionUtils.isEmpty(list) || (list.size() != homeIds.size())) {
                throw new CustomException(CodeEnum.HOME_PAGE_ID_ERROR);
            }
        }
    }

    /**
     * 检查权限树的完整性 (权限上级必需存在)
     * 〈〉
     *
     * @param list 检验菜单
     * @author XQ
     * @date 2019/7/31 19:06
     */
    private void checkMenuTree(List<SystemMenuDO> list) {
        Set<Integer> ids = new TreeSet<>();
        list.forEach(
                treeVO -> {
                    ids.add(treeVO.getId());
                });
        list.forEach(
                treeVO -> {
                    // 顶级没有父级不需要判断
                    if (!Integer.valueOf(0).equals(treeVO.getParentId())) {
                        if (!ids.contains(treeVO.getParentId())) {
                            throw new CustomException(CodeEnum.MENU_ID_ERROR);
                        }
                    }
                });
    }

    /**
     * 检查角色名称是否重复 (更新时需要排除自己本身)
     *
     * @param systemRoleDO 角色名称检验
     */
    private void checkRoleNameExists(SystemRoleDO systemRoleDO) {
        QueryWrapper<SystemRoleDO> roleQueryWrapper = new QueryWrapper<>();

        roleQueryWrapper.select("name");
        roleQueryWrapper.eq("name", systemRoleDO.getName());
        roleQueryWrapper.ne(systemRoleDO.getId() != null, "id", systemRoleDO.getId());
        roleQueryWrapper.last("limit 1");

        SystemRoleDO role1 = mapper.selectOne(roleQueryWrapper);

        if (role1 != null) {
            throw new CustomException(CodeEnum.ROLE_NAME_EXISTS);
        }
    }

    /**
     * 注销角色
     *
     * @param logoutSystemRoleDTO 注销角色
     * @return 注销角色
     */
    @Override
    public Result logoutSystemRole(LogoutSystemRoleDTO logoutSystemRoleDTO, TokenUser user) {
        QueryWrapper<SystemRoleDO> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("id", logoutSystemRoleDTO.getId());

        // 只有已生效状态才能注销
        queryWrapper.eq("status", RoleStatusEnum.EFFECTIVE.getValue());

        SystemRoleDO role = mapper.selectOne(queryWrapper);

        if (role == null) {
            throw new CustomException(CodeEnum.ROLE_ID_ERROR);
        }

        // 超级管理员不支持注销
        if (1 == role.getAdministrators()) {
            throw new CustomException(CodeEnum.ADMINISTRATORS_NOT_SUPPORTED);
        }

        // 角色下存在用户不支持注销
        QueryWrapper<SystemRoleUserDO> roleUserQueryWrapper = new QueryWrapper<>();

        roleUserQueryWrapper.eq("role_id", role.getId());
        roleUserQueryWrapper.last("limit 1");

        SystemRoleUserDO roleUser = systemRoleUserMapper.selectOne(roleUserQueryWrapper);

        if (roleUser != null) {
            throw new CustomException(CodeEnum.ROLE_USER_NOT_SUPPORTED_LOGOUT);
        }

        role.setStatus(RoleStatusEnum.LOGOUT.getValue());
        role.setOperationAccount(user.getUserAccount());
        role.setUpdateTime(new Date());
        mapper.updateById(role);
        /** 清空redis*/
        redisManageService.roleDisable(logoutSystemRoleDTO.getId());
        return Result.succeed();
    }

    /**
     * 新增角色
     *
     * @param addSystemRoleDTO 新增角色
     * @return 新增结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result saveSystemRole(AddSystemRoleDTO addSystemRoleDTO, TokenUser user) {
        SystemRoleDO systemRoleDO = new SystemRoleDO();
        BeanUtils.copyProperties(addSystemRoleDTO, systemRoleDO);
        Date date = new Date();
        /** 检验角色名的唯一性 */
        checkRoleNameExists(systemRoleDO);
        systemRoleDO.setCreateTime(date);
        systemRoleDO.setUpdateTime(date);
        /** 直接生效无需审批 */
        systemRoleDO.setStatus(RoleStatusEnum.EFFECTIVE.getValue());
        systemRoleDO.setOperationAccount(user.getUserAccount());
        mapper.insert(systemRoleDO);
        /** 数据检验 */
        checkMenuAndHomeModuleData(addSystemRoleDTO.getMenuIds(), addSystemRoleDTO.getHomepageModuleIds());
        /** 添加菜单权限及首页模块 */
        addMenuAndHomeModule(addSystemRoleDTO.getMenuIds(), addSystemRoleDTO.getHomepageModuleIds(), systemRoleDO.getId(), false);

        /** 清空redis*/
        redisManageService.roleAddORUpdate(systemRoleDO.getId());

        return Result.succeed();
    }


    /**
     * 修改角色
     *
     * @param updateSystemRoleDTO 修改角色
     * @return 修改
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateSystemRole(UpdateSystemRoleDTO updateSystemRoleDTO, TokenUser user) {
        QueryWrapper<SystemRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", updateSystemRoleDTO.getId());
        SystemRoleDO systemRoleUserDO = mapper.selectOne(queryWrapper);
        if (systemRoleUserDO == null) {
            throw new CustomException(CodeEnum.ROLE_ID_ERROR);
        }

        // 注销的角色不支持编辑
        if (RoleStatusEnum.LOGOUT.getValue().intValue() == systemRoleUserDO.getStatus().intValue()) {
            throw new CustomException(CodeEnum.LOGOUT_ROLE_NOT_SUPPORT_EDIT);
        }

        // 超级管理员不支持编辑
        if (1 == systemRoleUserDO.getAdministrators()) {
            throw new CustomException(CodeEnum.ADMINISTRATORS_NOT_SUPPORTED);
        }

        systemRoleUserDO.setName(updateSystemRoleDTO.getName());

        // 检查角色名称的唯一性
        checkRoleNameExists(systemRoleUserDO);
        systemRoleUserDO.setDescription(updateSystemRoleDTO.getDescription());
        systemRoleUserDO.setUpdateTime(new Date());
        systemRoleUserDO.setStatus(RoleStatusEnum.EFFECTIVE.getValue());

        systemRoleUserDO.setOperationAccount(user.getUserAccount());
        mapper.updateById(systemRoleUserDO);

        /** 数据检验 */
        checkMenuAndHomeModuleData(updateSystemRoleDTO.getMenuIds(), updateSystemRoleDTO.getHomepageModuleIds());

        /** 添加菜单权限及首页模块 */
        addMenuAndHomeModule(updateSystemRoleDTO.getMenuIds(),
                updateSystemRoleDTO.getHomepageModuleIds(),
                systemRoleUserDO.getId(),
                true);

        /** 清空redis*/
        redisManageService.roleAddORUpdate(updateSystemRoleDTO.getId());
        return Result.succeed();
    }


    /**
     * 查询角色详情查询
     *
     * @param querySystemRoleInfoDTO 详情
     * @return 查询结果
     */
    @Override
    public Result getSystemRoleInfo(QuerySystemRoleInfoDTO querySystemRoleInfoDTO) {
        QueryWrapper<SystemRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", querySystemRoleInfoDTO.getId());
        SystemRoleDO systemRoleDO = mapper.selectOne(queryWrapper);
        if (systemRoleDO == null) {
            throw new CustomException(CodeEnum.ROLE_ID_ERROR);
        }
        // 超级管理员菜单为全选状态
        boolean administrators = false;
        if (1 == systemRoleDO.getAdministrators()) {
            administrators = true;
        }
        SystemRoleDetailsVo systemRoleDetailsVo = new SystemRoleDetailsVo();
        BeanUtils.copyProperties(systemRoleDO, systemRoleDetailsVo);
        systemRoleDetailsVo.setAuthModules(getAuthModule(systemRoleDO.getId(), administrators, querySystemRoleInfoDTO.getOnlyChecked()));
        return Result.succeed(systemRoleDetailsVo);
    }


    /**
     * 功能描述: 查询出所有模块<br>
     * 〈0 查看  1 编辑  2 新增〉
     *
     * @return 需要展示的工作台模块以及模块下对应的菜单
     * @author XQ
     * @date 2019/8/1 10:11
     */
    @Override
    public List<AuthModuleVO> getAllModule(Integer roleId, Integer type) {
        /**创建响应对应模块菜单*/
        List<AuthModuleVO> moduleTree = new ArrayList<>();
        QueryWrapper<SystemModuleDO> queryWrapper = new QueryWrapper<>();

        List<SystemModuleDO> lists = systemModuleMapper.selectList(queryWrapper);
        /**查出所有模块*/
        lists.forEach(module -> {
            AuthModuleVO authModuleVO = new AuthModuleVO();
            authModuleVO.setId(module.getId());
            authModuleVO.setModuleName(module.getModuleName());
            moduleTree.add(authModuleVO);
        });

        moduleTree.forEach(authModuleVO -> {
            authModuleVO.setMenus(menuService.getAllMenuTree(authModuleVO.getId(), roleId, type));
            authModuleVO.setHomepageModules(systemHomepageModuleService.getHomepageModuleList(authModuleVO.getId(), roleId, type));
        });
        return moduleTree;
    }


    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param roleId 角色ID
     * @return List<AuthModuleVO> 模块
     * @author XQ
     * @date 2019/7/31 15:49
     */
    @Override
    public List<AuthModuleVO> getAuthModule(Integer roleId, boolean administrators, Integer onlyChecked) {
        // 查询所有模块
        QueryWrapper<SystemModuleDO> queryWrapper = new QueryWrapper<>();
        List<SystemModuleDO> openModules = systemModuleMapper.selectList(queryWrapper);
        // 菜单树
        List<MenuTreeVO> treeMenu = getTreeMenu(roleId, administrators, onlyChecked);

        Set<Integer> checkdHomepageModuleIds = new HashSet<>();
        // 角色拥有的首页模块集合
        if (roleId != null && onlyChecked.intValue() == 1) {
            QueryWrapper<SystemRoleHomepageModuleDO> roleIdQueryWrapper = new QueryWrapper<>();
            roleIdQueryWrapper.eq("role_id", roleId);
            List<SystemRoleHomepageModuleDO> roleHomepageModules = systemRoleHomepageModuleMapper.selectList(roleIdQueryWrapper);
            if (!CollectionUtils.isEmpty(roleHomepageModules)) {
                roleHomepageModules.forEach(homepageModule -> {
                    checkdHomepageModuleIds.add(homepageModule.getHomepageModuleId());
                });
            }

        }

        // 查看详情和审核页面只需要展示选中的菜单  编辑页面要展示选中和未选中菜单
        Set<Integer> homePageIds = null;
        if (!administrators && roleId != null && Integer.valueOf(1).equals(onlyChecked)) {
            homePageIds = checkdHomepageModuleIds;
        }
        QueryWrapper<SystemHomepageModuleDO> homePageQueryWrapper = new QueryWrapper<>();
        homePageQueryWrapper.in(!CollectionUtils.isEmpty(homePageIds), "id", homePageIds);
        homePageQueryWrapper.ne("is_default", 0);
        homePageQueryWrapper.notLike("name", "审批");
        // 所有首页模块集合
        List<SystemHomepageModuleDO> companyAllHomePageList = systemHomepageModuleMapper.selectList(homePageQueryWrapper);

        JacksonUtil.dumnToPrettyJsonInfo("查看/编辑角色查询到的模块：", companyAllHomePageList);
        List<HomePageModuleVo> homePageModuleVoLists = new ArrayList<>();
        if (!CollectionUtils.isEmpty(companyAllHomePageList)) {
            companyAllHomePageList.forEach(homePage -> {
                HomePageModuleVo homePageModuleVo = new HomePageModuleVo();
                BeanUtils.copyProperties(homePage, homePageModuleVo);
                //查看时只展示已选择中的模块
                if (administrators || checkdHomepageModuleIds.contains(homePage.getId())) {
                    homePageModuleVo.setChecked(true);
                    homePageModuleVoLists.add(homePageModuleVo);
                } else {
                    //编辑时展示所有默认工作台
                    homePageModuleVo.setChecked(false);
                    homePageModuleVoLists.add(homePageModuleVo);
                }
            });
        }

        List<AuthModuleVO> authModuleLists = new ArrayList<>();
        // 权限菜单分配到指定模块下
        openModules.forEach(systemModuleDO -> {
            AuthModuleVO authModuleVO = new AuthModuleVO();
            BeanUtils.copyProperties(systemModuleDO, authModuleVO);
            authModuleVO.setMenus(new ArrayList<>());
            authModuleVO.setHomepageModules(new ArrayList<>());
            treeMenu.forEach(menu -> {
                if (menu.getModuleId().equals(authModuleVO.getId())) {
                    authModuleVO.getMenus().add(menu);
                }
            });
            homePageModuleVoLists.forEach(homepageModule -> {
                if (homepageModule.getModuleId().equals(authModuleVO.getId())) {
                    authModuleVO.getHomepageModules().add(homepageModule);
                }
            });
            authModuleLists.add(authModuleVO);
        });

        List<AuthModuleVO> modules = authModuleLists.stream()
                .filter(module -> {
                    if (!CollectionUtils.isEmpty(module.getMenus())
                            || !CollectionUtils.isEmpty(module.getHomepageModules())) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        return modules;
    }


    /**
     * 生产树形结构的权限菜单
     *
     * @param roldId         角色id 新增为null 查询为true (需要检查是否选中)
     * @param administrators 是否为超级管理员 超级管理员默认全部菜单选中
     * @param onlyChecked    是否只展示选中菜单 1 是 0
     * @return 菜单权限否
     */
    private List<MenuTreeVO> getTreeMenu(Integer roldId, boolean administrators, Integer onlyChecked) {
        /**选中菜单集合*/
        Set<Integer> checkdMenuIds = new HashSet<>();
        // 角色拥有的权限菜单 查询详情页面需要 新增页面不需要
        if (roldId != null) {
            QueryWrapper<SystemRoleMenuDO> roleIdQueryWrapper = new QueryWrapper<>();
            roleIdQueryWrapper.eq("role_id", roldId);
            List<SystemRoleMenuDO> roleMenus = systemRoleMenuMapper.selectList(roleIdQueryWrapper);
            if (!CollectionUtils.isEmpty(roleMenus)) {
                roleMenus.forEach(roleMenu -> {
                    checkdMenuIds.add(roleMenu.getMenuId());
                });
            }
        }
        // 查看详情和审核页面只需要展示选中的菜单  编辑页面要展示选中和未选中菜单
        Set<Integer> menuIds = null;
        if (!administrators && roldId != null && Integer.valueOf(1).equals(onlyChecked)) {
            menuIds = checkdMenuIds;
        }
        QueryWrapper<SystemMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(!CollectionUtils.isEmpty(menuIds), "id", menuIds);
//        queryWrapper.orderByDesc("sort");
        queryWrapper.orderByAsc("sort");
        // 角色不为null时查询该角色下所有菜单   为null时 查询所有菜单
        List<SystemMenuDO> companyAllMenuList = systemMenuMapper.selectList(queryWrapper);
        // 树形结构
        List<MenuTreeVO> tree = new ArrayList<>();
        //装换存储对象
        List<MenuTreeVO> allMenuList = new ArrayList<>();
        companyAllMenuList.forEach(menu -> {
            MenuTreeVO menuVO = new MenuTreeVO();
            BeanUtils.copyProperties(menu, menuVO);
            allMenuList.add(menuVO);
        });
        // 设置选中状态 并格式成树形结构
        if (!CollectionUtils.isEmpty(allMenuList)) {
            Map<Integer, MenuTreeVO> maps = new HashMap<>();
            allMenuList.forEach(menu -> {
                // 查询详情页面需要选中 新增页面不需要
                if (administrators || (roldId != null && checkdMenuIds.contains(menu.getId()))) {
                    menu.setChecked(true);
                } else {
                    menu.setChecked(false);
                }
                menu.setChildren(new ArrayList<>());
                // 格式化成树形
                maps.put(menu.getId(), menu);
            });

            // 格式化成树形
            allMenuList.forEach(menu -> {
                MenuTreeVO parent = maps.get(menu.getParentId());
                if (parent == null) {
                    tree.add(menu);
                } else {
                    parent.getChildren().add(menu);
                }
            });
        }
        return tree;
    }


    /**
     * 查询角色列表
     *
     * @param querySystemRoleDTO 分页条件
     * @return 分页后查询的结果集
     */
    @Override
    public Result<PageResult<SystemRoleListVO>> getSystemRoleListPage(QuerySystemRoleDTO querySystemRoleDTO) {
        /** 查询条件 */
        QueryWrapper<SystemRoleDO> queryWrapper = new QueryWrapper<>();
        if (querySystemRoleDTO.getName() != null) {
            queryWrapper.like("name", querySystemRoleDTO.getName());
        }
        if (querySystemRoleDTO.getStatus() != null) {
            queryWrapper.eq("status", querySystemRoleDTO.getStatus());
        }
        /*日期控制*/
        if ((querySystemRoleDTO.getStartTime() != null) && (querySystemRoleDTO.getEndTime() != null)) {
            /*开始日期不能大于结束日期*/
            if (querySystemRoleDTO.getStartTime().after(querySystemRoleDTO.getEndTime())) {
                throw new CustomException(CodeEnum.ROLE_DATE_COMPARETO);
            }
            queryWrapper.between("create_time", querySystemRoleDTO.getStartTime(), querySystemRoleDTO.getEndTime());
        } else if ((querySystemRoleDTO.getStartTime() != null) || (querySystemRoleDTO.getEndTime() != null)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            queryWrapper.between("create_time", querySystemRoleDTO.getStartTime() == null ? "" : querySystemRoleDTO.getStartTime()
                    , querySystemRoleDTO.getEndTime() == null ? format.format(new Date()) : querySystemRoleDTO.getEndTime());
        }
        queryWrapper.orderByDesc("update_time");
        /** 分页结果查询 */
        IPage<SystemRoleDO> page = mapper.selectPage(new Page<>(querySystemRoleDTO.getPageNum(), querySystemRoleDTO.getPageSize()), queryWrapper);
        List<SystemRoleDO> list = page.getRecords();
        List<SystemRoleListVO> systemRoleLists = new ArrayList<>();
        for (SystemRoleDO systemRoleDO : list) {
            SystemRoleListVO systemRoleListVO = new SystemRoleListVO();
            BeanUtils.copyProperties(systemRoleDO, systemRoleListVO);
            /* 统计该角色下用户数 */
            systemRoleListVO.setStatus(systemRoleDO.getStatus());
            systemRoleListVO.setRoleUserCount(systemRoleUserMapper.selectCount(new QueryWrapper<SystemRoleUserDO>().eq("role_id", systemRoleDO.getId())));
            systemRoleLists.add(systemRoleListVO);
        }
        PageResult<SystemRoleListVO> pageResult = new PageResult<>();
        pageResult.setRecords(systemRoleLists);
        pageResult.setCurrentPage(Integer.parseInt(page.getCurrent() + ""));
        pageResult.setPageSize(Integer.parseInt(page.getSize() + ""));
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        List<TableHeaderVO> tableHeaders = new ArrayList<>();
        tableHeaders.add(new TableHeaderVO("name", "角色名称"));
        tableHeaders.add(new TableHeaderVO("roleUserCount", "角色下成员人数"));
        tableHeaders.add(new TableHeaderVO("status", "角色状态"));
        tableHeaders.add(new TableHeaderVO("administrators", "是否超级管理员"));
        tableHeaders.add(new TableHeaderVO("operationAccount", "操作人"));
        tableHeaders.add(new TableHeaderVO("createTime", "创建时间"));
        tableHeaders.add(new TableHeaderVO("updateTime", "更新信息"));
        pageResult.setTableHeaders(tableHeaders);
        return Result.succeed(pageResult);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRoleUserByUserId(Integer userId, @Nullable Set<Integer> roleIds) {

        Objects.requireNonNull(userId, "根据用户id更新用户角色关联表，用户id不能为空");

        if (!CollectionUtils.isEmpty(roleIds)) {
            QWrapper<SystemRoleDO> qWrapper = new QWrapper<>();
            qWrapper.in(SystemRoleDO.Fields.id, roleIds).eq(SystemRoleDO.Fields.status, 1);
            Collection<SystemRoleDO> systemRoleDOs = baseMapper.selectList(qWrapper);
            // 通过角色id没有查询到角色抛出异常
            if (systemRoleDOs == null) {
                throw new ServiceException(CodeEnum.SAAS_OPERA_MANAGER_20212008, StringUtils.join(roleIds, ","));
            }
            if (systemRoleDOs.size() != roleIds.size()) {
                Set<Integer> copyOfRoleIds = Sets.newHashSet(roleIds);
                for (SystemRoleDO systemRoleDO : systemRoleDOs) {
                    if (systemRoleDO.getStatus() == 1) {
                        copyOfRoleIds.remove(systemRoleDO.getId());
                    }
                }
                if (copyOfRoleIds.size() != 0) {
                    throw new ServiceException(CodeEnum.SAAS_OPERA_MANAGER_20212008, StringUtils.join(copyOfRoleIds, ","));
                }
            }
        }

        // 先全部删除，再插入
        QWrapper<SystemRoleUserDO> qWrapper = new QWrapper<>();
        qWrapper.eq(SystemRoleUserDO.Fields.userId, userId);
        this.systemRoleUserMapper.delete(qWrapper);
        redisService.remove("saAsOperationUserRole:"+String.valueOf(userId));
        List<SystemRoleUserDO> systemRoleUserDOS = this.systemRoleUserMapper.selectList(qWrapper);
        if (!CollectionUtils.isEmpty(systemRoleUserDOS)) {
            throw new ServiceException(CodeEnum.SAAS_OPERA_MANAGER_20212014, userId);
        }
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        String roIds = "";
        String sk = "";
        for (Integer roleId : roleIds) {
            int count = this.systemRoleUserMapper.insert(new SystemRoleUserDO(roleId, userId));
            if (count != 1) {
                throw new ServiceException(CodeEnum.SAAS_OPERA_MANAGER_20212009, roleId);
            }
            roIds += sk + roleId;
            sk = ",";
        }
        redisService.set("saAsOperationUserRole:"+String.valueOf(userId),roIds);

    }

    @Override
    public List<RoleIdNameVo> listRoleIdNameEnable() {
        return this.baseMapper.listRoleIdNameEnable();
    }

    /**
     * 判断用户是否超级管理员
     *
     * @param userId : 用户id
     * @return Result
     * @author WFZ
     * @date 2019/7/31 17:02
     */
    @Override
    public Boolean isAdministratorByUserId(Serializable userId) {
        List<SystemRoleDO> roleDOList = baseMapper.listSystemRoleByUserId(userId);
        if (PublicUtil.isNotEmpty(roleDOList)) {
            for (SystemRoleDO role : roleDOList) {
                if (role.getAdministrators().intValue() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<RoleNameVo> getRoleNameUserIdList(String userIdsStr) {
        if (StringUtils.isBlank(userIdsStr)) {
            return Collections.emptyList();
        }
        return baseMapper.getRoleNameUserIdList(userIdsStr);
    }
}















