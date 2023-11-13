package com.itheima.medical.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.medical.common.R;
import com.itheima.medical.entity.User;
import com.itheima.medical.service.Medical_HistoryService;
import com.itheima.medical.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private UserService userService;

    //1.注册接口
    @PostMapping
    public R<String> save(@RequestBody User user) {
        log.info("新增员工，员工信息：{}", user.toString());

        //设置初始密码为123456，并且进行md5加密的处理（因为新增员工的前端没有密码的设置,所以密码是空的，下面这些手动设置的都是）
        //这里注意一点，你这个密码存到数据库的时候肯定必须是要加密的，不可能把原始密码存进去。
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //注意这里就是mybitas-plus直接直接在mapper层就封装了最基本的增删查改的操作了（通过继承BaseMapper），所以这里直接调用就好了
        // 注意这些封装好的方法都是写好名字的，不能自己定义，要用的时候要去查一查
        userService.save(user);

        return R.success("新增用户信息成功");
    }

    //2.登录接口
    @PostMapping("/login")
        //这里不是通过jwt令牌了，没有token了，所以这里我们需要HttpServletRequest request这个将请求强制转为HttpServletRequest，然后存储在session里面
        public R<User> login(HttpServletRequest request, @RequestBody User user) {
            //以后应该先把业务逻辑理清楚了，然后直接放在这里，这样我们就可以根据文字来翻译代码了！


            //1.将页面提交的密码进行md5加密处理

            //这里是将我们前端请求封装到employee里面的密码拿到了
            String password = user.getPassword();
            //这里变量前面不加类型，相当于直接赋值，而加类型则是说明我们这里创建一个新的变量
            //然后这里是将我们页面传过来的密码转成一个byte数组（也就是md5加密处理！！！）
            password = DigestUtils.md5DigestAsHex(password.getBytes());

            //2.根据页面提交的用户名user来查询数据库

            //这里是包装一个条件构造器
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            //这里使用了queryWrapper.eq构建了一个查询条件（基于mybitas-plus）
            //非常重要！！！————》queryWrapper.eq()是表示在查询条件中添加一个等于的条件，第一个参数是比较的字段名，第二个参数是比较的值
            // (理解：你这个添加的查询条件，肯定要有个查询的依据吧，不然你根据什么查？所以括号里面要添加上你需要查询的字段名和对应的值）
            queryWrapper.eq(User::getUsername, user.getUsername());

            //mybitas-plus可以直接调用mapper进行数据库查询（但是调用mapper的时候都是统一调用service层的那个接口！！！！！！！！！！！！这个非常重要）
            //这里就是用在查询数据库（这里的调用逻辑是这样的，首先是employeeService它被EmployeeServiceImpl实现，
            // 然后EmployeeServiceImpl又继承了ServiceImpl<EmployeeMapper, Employee>这个，
            // 然后EmployeeMapper里面又继承了BaseMapper<Employee>，这个里面就有最基本的查询语句。）

            //重要！！！————》基于上述逻辑，employeeService中的getOne方法根据queryWrapper.eq条件进行查询，然后将结果封装在emp对象里面
            //之所以调用getOne是因为这里的username是一个unique的参数（查询一般查的都是唯一参数，因为要确保查询来不能有错）
            User user1 = userService.getOne(queryWrapper);

            //3.如果没有查询到则返回登录失败结果
            if (user1 == null) {
                return R.error("登录失败");
            }

            //4.密码比对，如果不一致则返回登录失败的结果（注意这里的逻辑，是一致放行，不一致运行，所以前面的非号一定要注意！！！！！）
            if (!user1.getPassword().equals(password)) {
                return R.error("登录失败");
            }

            //6.登录成功，将员工id存入session并返回登录成功的结果

            //通过前面定义的request将员工的id存入session
            //session的修改方法————》request.getSession()然后直接对其进行需要的操作加上+Attribute，这里的employee是一个属性，被设置成了emp.getId()的值
            request.getSession().setAttribute("user", user1.getId());
            //再将后端从数据库中查出来的信息响应回去
            return R.success(user1);
        }

        //3.从后台退出的方法
        @PostMapping("/logout")
        //因为我们一会要操作参数，需要在session中删除id，所以我们传入的参数应该是HttpServletRequest request
        public R<String> logout(HttpServletRequest request) {
            //请理session中保存的当前登录员工的id
            //request的用法就是getSession()然后直接对其进行需要的操作Attribute，需要在括号里面指定一个对象（与上面同理）
            request.getSession().removeAttribute("user");
            return R.success("退出成功");
        }
    }
