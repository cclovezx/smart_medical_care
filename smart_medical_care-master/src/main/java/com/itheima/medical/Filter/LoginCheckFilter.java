package com.itheima.medical.Filter;

import com.alibaba.fastjson.JSON;
import com.itheima.medical.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//检查我们用户是否已经完成了登录，这是一个过滤器
@Slf4j
//这里是定义了我们过滤器的名称以及拦截路径/*代表全部拦截
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
//要使用过滤器就需要继承这个Filter这个接口
public class LoginCheckFilter implements Filter {

    //实现这个Filter接口的方法
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //ServletRequest servletRequest就是从前端拿到的请求，ServletResponse servletResponse自然也就是响应回去的数据
        //强制转型，将servletRequest从ServletRequest类型转换成HttpServletRequest的类型，并且重新命名为request(servletResponse同理）
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //Filter里面的放行操作，就是利用filterChain.doFilter然后把请求和响应都放入进去!
        //filterChain.doFilter(request,response);


        //1.获取本次请求的url
        String requestURL = request.getRequestURI();
        log.info("拦截到请求：{}", requestURL);

        //这些路径是不需要处理的路径（通配符的方法）
        String[] urls = new String[]{
                "/system",
                //测试的时候记得加上，不然会被拦截
                "/analyse/**",
                "/system/login",
                "/system/logout",
                "/common/**"
        };

        //2.判断本次请求是否需要处理,如果不需要处理则直接放行
        boolean check = Check(urls,requestURL);
        if (check){
            log.info("本次请求{}不需要处理", requestURL);
            filterChain.doFilter(request,response);
            //注意这两个return是必须要加的，如果你这里不加的话，比如这里你在判断完是一个登录请求之后，放行之后必须要return，不然就会继续往下走进行下面的判断，但这时候
            //你还没有登录，所以请求路径检查和登录session的id校验，一定要加return；语句，而且还会导致多个线程同时访问同一个输出流对象。
            return;
        }

        //3.判断登录状态，如果已经登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);

            //获取当前的线程id(浏览器每发送一个请求，服务端就会分配一个线程）
            //但是LoginFilter过滤器，EmployeeConller的update（这里是以update为例），还有MyMetaObjecthandler这三者同属于一个线程（先进过滤器，再到原数据处理器）

            //Long id =Thread.currentThread().getId();
            //log.info("线程id为：{}",id);
            //这个return是必须加的
            return;
        }

        //4.如果未登录则返回登录结果,通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        //这段代码的含义是通过将R对象转换成一个json格式的数据，然后通过write这个输出流写回去
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    //路径匹配器，支持通配符（相当于一个工具类）
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    //进行路径匹配
    public boolean Check(String[] urls,String requestURL){
        //对传进来的String urls【】进行遍历（这里的“：”是指将urls元素的值赋值给变量url，然后执行循环体中的代码）
        for (String url : urls) {
            //调用路径匹配器进行路径匹配
            boolean match = PATH_MATCHER.match(url,requestURL);
            if(match){
                return true;
            }
        }
        return false;
    }

}
