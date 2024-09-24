package org.seckill.mapper;

import org.seckill.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * &#064;Mapper、@Select、@Update——MyBatis注解
 */
@Mapper
public interface UserMapper {

    @Select("select * from sk_user where id = #{id}")
    User getById(@Param("id") long id);

    @Update("update sk_user set password = #{password} where id = #{id}")
    void update(User toBeUpdate);
}
