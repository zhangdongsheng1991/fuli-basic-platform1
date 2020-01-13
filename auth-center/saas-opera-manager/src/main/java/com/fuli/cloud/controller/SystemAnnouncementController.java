package com.fuli.cloud.controller;

import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.commons.utils.JacksonUtil;
import com.fuli.cloud.commons.utils.QWrapper;
import com.fuli.cloud.commons.utils.UWrapper;
import com.fuli.cloud.dto.BatchUpdateReadDTO;
import com.fuli.cloud.dto.QueryAnnouncementUserListDTO;
import com.fuli.cloud.model.SystemAnnouncementUserDO;
import com.fuli.cloud.service.SystemAnnouncementService;
import com.fuli.cloud.service.SystemAnnouncementUserService;
import com.fuli.cloud.vo.SystemAnnouncementVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author chenyi
 * @date 2019/8/2
 */
@Api(tags = "系统消息 - 陈毅")
@RequestMapping("/systemAnnouncement")
@RestController
public class SystemAnnouncementController extends BaseController {

    @Resource
    private SystemAnnouncementUserService systemAnnouncementUserService;

    @Resource
    private SystemAnnouncementService systemAnnouncementService;

    @PostMapping("/batchSetReaded")
    @ApiOperation("批量设置系统消息已读")
    public Result<?> batchSetReaded(@Validated @RequestBody BatchUpdateReadDTO batchUpdateReadDto) {

        JacksonUtil.dumnToPrettyJsonDebug("根据用户id和公告消息id集合查询，更新已读。参数：", batchUpdateReadDto);

        Set<Long> ids = batchUpdateReadDto.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            return Result.failed("系统消息id不能为空");
        }
        SystemAnnouncementUserDO saasAnnouncement = new SystemAnnouncementUserDO();
        saasAnnouncement.setReadFlag(1);
        UWrapper<SystemAnnouncementUserDO> updateWrapper = new UWrapper<SystemAnnouncementUserDO>();
        updateWrapper.eq(SystemAnnouncementUserDO.Fields.userId, this.getSystemUserId())
                .in(SystemAnnouncementUserDO.Fields.announcementId, ids);

        return this.systemAnnouncementUserService.update(saasAnnouncement, updateWrapper) ? Result.succeed() : Result.failed();
    }

    @GetMapping("/getUnreadCount")
    @ApiOperation("获取用户未读消息数量")
    public Result<?> getUnreadCount() {
        Integer loginUserId = getSystemUserId();
        JacksonUtil.dumnToPrettyJsonDebug("获取用户的未读系统公告,参数：", loginUserId);
        QWrapper<SystemAnnouncementUserDO> qWrapper = new QWrapper<SystemAnnouncementUserDO>();
        qWrapper.eq(SystemAnnouncementUserDO.Fields.userId, loginUserId)
                .eq(SystemAnnouncementUserDO.Fields.readFlag, 0);
        return Result.succeed(this.systemAnnouncementUserService.count(qWrapper));
    }

    @ApiOperation(value = "查询集合")
    @PostMapping("/getList")
    public Result<List<SystemAnnouncementVO>> getList(@RequestBody @Validated QueryAnnouncementUserListDTO queryAnnouncementUserListDTO) {

        queryAnnouncementUserListDTO.setUserId(getSystemUserId());

        JacksonUtil.dumnToPrettyJsonDebug("根据参数，查询集合 :", queryAnnouncementUserListDTO);

        return Result.succeed(this.systemAnnouncementService.getSystemAnnouncementVOList(queryAnnouncementUserListDTO));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/listOnePage")
    public Result<PageResult<SystemAnnouncementVO>> listOnePage(@RequestBody @Validated QueryAnnouncementUserListDTO queryAnnouncementUserListDTO) {

        queryAnnouncementUserListDTO.setUserId(getSystemUserId());

        JacksonUtil.dumnToPrettyJsonDebug("分页查询 :", queryAnnouncementUserListDTO);

        PageInfo<SystemAnnouncementVO> pageInfo = new PageInfo<>(
                this.systemAnnouncementService.listOnePage(queryAnnouncementUserListDTO));
        PageResult<SystemAnnouncementVO> pageResult = PageResult.getPageResult(pageInfo);
        return Result.succeed(pageResult);
    }
}
