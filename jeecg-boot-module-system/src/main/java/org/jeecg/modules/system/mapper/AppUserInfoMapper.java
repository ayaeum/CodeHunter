package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.AppUserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @description 用户信息Mapper
 * @author caiguapi
 * @date 2022-08-20
 */
@Mapper
@Repository
public interface AppUserInfoMapper extends BaseMapper<AppUserInfo> {

    @Select(
            "<script>select t0.* from app_user_info t0 " +
                    //add here if need left join
                    "where 1=1" +
                    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
                    "<when test='userid!=null and userid!=&apos;&apos; '> and t0.userid=#{userid}</when> " +
                    "<when test='username!=null and username!=&apos;&apos; '> and t0.username=#{username}</when> " +
                    "<when test='accesstoken!=null and accesstoken!=&apos;&apos; '> and t0.accesstoken=#{accesstoken}</when> " +
                    "<when test='datatime!=null and datatime!=&apos;&apos; '> and t0.datatime=#{datatime}</when> " +
                    "<when test='giteeaccount!=null and giteeaccount!=&apos;&apos; '> and t0.giteeaccount=#{giteeaccount}</when> " +
                    "<when test='giteepassword!=null and giteepassword!=&apos;&apos; '> and t0.giteepassword=#{giteepassword}</when> " +
                    "<when test='clientid!=null and clientid!=&apos;&apos; '> and t0.clientid=#{clientid}</when> " +
                    "<when test='clientsecret!=null and clientsecret!=&apos;&apos; '> and t0.clientsecret=#{clientsecret}</when> " +
                    //add here if need page limit
                    //" limit ${page},${limit} " +
                    " </script>")
    List<AppUserInfo> pageAll(AppUserInfo queryParamDTO,int page,int limit);

    @Select("<script>select count(1) from app_user_info t0 " +
            //add here if need left join
            "where 1=1" +
            "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
            "<when test='userid!=null and userid!=&apos;&apos; '> and t0.userid=#{userid}</when> " +
            "<when test='username!=null and username!=&apos;&apos; '> and t0.username=#{username}</when> " +
            "<when test='accesstoken!=null and accesstoken!=&apos;&apos; '> and t0.accesstoken=#{accesstoken}</when> " +
            "<when test='datatime!=null and datatime!=&apos;&apos; '> and t0.datatime=#{datatime}</when> " +
            "<when test='giteeaccount!=null and giteeaccount!=&apos;&apos; '> and t0.giteeaccount=#{giteeaccount}</when> " +
            "<when test='giteepassword!=null and giteepassword!=&apos;&apos; '> and t0.giteepassword=#{giteepassword}</when> " +
            "<when test='clientid!=null and clientid!=&apos;&apos; '> and t0.clientid=#{clientid}</when> " +
            "<when test='clientsecret!=null and clientsecret!=&apos;&apos; '> and t0.clientsecret=#{clientsecret}</when> " +
            " </script>")
    int countAll(AppUserInfo queryParamDTO);

}
