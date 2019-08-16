package com.example.demo.clouddo.dao;

import com.example.demo.clouddo.domain.UserDO;
import java.util.List;

/**
 * Created by Administrator on 2019/7/5.
 */
//@Repository
//public interface UserDao extends JpaRepository<UserDODO,Long> {
//
//
//}
public interface UserDao{
    /**
     * 新增用户
     * @param
     * @return
     */
    int save (UserDO userDO);

    /**
     * 更新用户信息
     * @param
     * @return
     */
    int update (UserDO userDO);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById (Long id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    UserDO selectById (Long id);

    /**
     * 查询所有用户信息
     * @return
     */
    List<UserDO> selectAll ();
}
