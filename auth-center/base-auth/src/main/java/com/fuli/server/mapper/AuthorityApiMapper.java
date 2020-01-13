package com.fuli.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.auth.common.model.GatherApi;
import com.fuli.server.model.entity.BaseApiDO;
import com.fuli.server.model.vo.AuthorityApiVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuthorityApiMapper extends BaseMapper<BaseApiDO> {
    /**
     * 根据权限组id查询APIS
     * @param groudId
     * @return
     */
    List<AuthorityApiVO> getApisByGroudId(@Param("groudId")String groudId);

    /**
     * 权限组绑定api
     * @param groupId
     * @param apiIds
     */
    int authorityBindApi(@Param("groudId")String groupId, @Param("apiIds")String[] apiIds);

    /**
     * 批量保存apis
     * @param apis
     */
    int saveApis(@Param("apis") List<GatherApi> apis);
}
