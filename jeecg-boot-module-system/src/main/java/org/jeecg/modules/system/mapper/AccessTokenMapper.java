package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.AccessToken;

import java.util.List;
/**
 * @description 用户信息Mapper
 * @author caiguapi
 * @date 2022-08-20
 */
@Mapper
public interface AccessTokenMapper extends BaseMapper<AccessToken> {

    @Select(
            "<script>select t0.* from user_info t0 " +
                    //add here if need left join
                    "where 1=1" +
                    "<when test='userId!=null and userId!=&apos;&apos; '> and t0.user_id=#{userId}</when> " +
                    "<when test='userName!=null and userName!=&apos;&apos; '> and t0.user_name=#{userName}</when> " +
                    "<when test='accessToken!=null and accessToken!=&apos;&apos; '> and t0.access_token=#{accessToken}</when> " +
                    "<when test='createTime!=null and createTime!=&apos;&apos; '> and t0.create_time=#{createTime}</when> " +
                    "<when test='userEmail!=null and userEmail!=&apos;&apos; '> and t0.user_email=#{userEmail}</when> " +
                    "<when test='userPassword!=null and userPassword!=&apos;&apos; '> and t0.user_password=#{userPassword}</when> " +
                    "<when test='clientId!=null and clientId!=&apos;&apos; '> and t0.client_id=#{clientId}</when> " +
                    "<when test='clientSecret!=null and clientSecret!=&apos;&apos; '> and t0.client_secret=#{clientSecret}</when> " +
                    //add here if need page limit
                    //" limit ${page},${limit} " +
                    " </script>")
    List<AccessToken> pageAll(AccessToken queryParamDTO,int page,int limit);

    @Select("<script>select count(1) from user_info t0 " +
            //add here if need left join
            "where 1=1" +
            "<when test='userId!=null and userId!=&apos;&apos; '> and t0.user_id=#{userId}</when> " +
            "<when test='userName!=null and userName!=&apos;&apos; '> and t0.user_name=#{userName}</when> " +
            "<when test='accessToken!=null and accessToken!=&apos;&apos; '> and t0.access_token=#{accessToken}</when> " +
            "<when test='createTime!=null and createTime!=&apos;&apos; '> and t0.create_time=#{createTime}</when> " +
            "<when test='userEmail!=null and userEmail!=&apos;&apos; '> and t0.user_email=#{userEmail}</when> " +
            "<when test='userPassword!=null and userPassword!=&apos;&apos; '> and t0.user_password=#{userPassword}</when> " +
            "<when test='clientId!=null and clientId!=&apos;&apos; '> and t0.client_id=#{clientId}</when> " +
            "<when test='clientSecret!=null and clientSecret!=&apos;&apos; '> and t0.client_secret=#{clientSecret}</when> " +
            " </script>")
    int countAll(AccessToken queryParamDTO);

}
