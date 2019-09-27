package com.hs.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hs.dao.CodesMapper;
import com.hs.dao.UsersMapper;
import com.hs.pojo.Codes;
import com.hs.pojo.Users;
import com.hs.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CodesMapper codesMapper;

    /**
     * 全部数据
     *
     * @return
     */
    public List<Users> findAll() {
        return usersMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public Users findById(String id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    /**
     * 增加
     *
     * @param users
     */
    @Transactional
    public void add(Users users) {
        usersMapper.insert(users);
    }

    /**
     * 修改
     *
     * @param users
     */
    @Transactional
    public void update(Users users) {
        usersMapper.updateByPrimaryKey(users);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Transactional
    public int delete(String id) {
        return usersMapper.deleteByPrimaryKey(id);
    }


    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    public Page<Users> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Users>) usersMapper.selectAll();
    }

    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    public List<Users> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return usersMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();

        if (searchMap != null) {
            // id
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andLike("id", "%" + searchMap.get("id") + "%");
            }
            // username
            if (searchMap.get("username") != null && !"".equals(searchMap.get("username"))) {
                criteria.andLike("username", "%" + searchMap.get("username") + "%");
            }
            // usertype
            if (searchMap.get("usertype") != null && !"".equals(searchMap.get("usertype"))) {
                criteria.andLike("usertype", "%" + searchMap.get("usertype") + "%");
            }


        }
        return example;
    }

    /**
     * 条件+分页查询
     *
     * @param searchMap 查询条件
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    public Page<Users> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        return (Page<Users>) usersMapper.selectByExample(example);
    }


    @Transactional
    public void resetPwd(String code, String passwd) {
        // 根据code码查询出用户
        Codes codes = codesMapper.selectOne(new Codes(code, null));
        Users u = new Users();
        u.setEmail(codes.getEmail());
        u = usersMapper.selectOne(u);
        u.setPasswd(passwd);
        usersMapper.updateByPrimaryKey(u);
        // 删除codes码
        codesMapper.delete(codes);
    }

    public Users findByName(String username) {
        Users u = new Users();
        u.setUsername(username);
        return usersMapper.selectOne(u);
    }

    public boolean findEmail(String email) {
        Users euser = new Users();
        euser.setEmail(email);
        if (usersMapper.selectOne(euser) != null) {
            return true;
        }
        return false;
    }

    /**
     * 发送邮件
     *
     * @param email
     * @throws MessagingException
     */
    @Transactional
    public void sendEmail(String email) throws MessagingException {
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        String msg = "您好，我们的系统收到一个请求，说您希望通过电子邮件重新设置您在教育资源管理平台的密码。您可以点击下面的链接重设密码：\n" +
                "http://192.168.0.108:8080/resetpwd?code=" + code + "\n" +
                "请在30分钟内执行该操作。如果这个请求不是由您发起的，那没问题，您不用担心，您可以安全地忽略这封邮件。\n" +
                "如果您有任何疑问，可以回复这封邮件向我们提问。谢谢！";
        MailUtils.sendMail(email, "找回密码", msg);
        /**
         * code码与邮箱进行持久化绑定
         */
        /*Users users = new Users();
        users.setEmail(email);
        users = usersMapper.selectOne(users);*/
        Codes codes = codesMapper.selectOne(new Codes(null, email));
        if (codes != null) {
            codes.setCode(code);
            codesMapper.updateByPrimaryKey(codes);
        } else {
            codesMapper.insert(new Codes(code, email));
        }
    }
}
