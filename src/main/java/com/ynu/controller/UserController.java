package com.ynu.controller;

import com.ynu.controller.ex.*;
import com.ynu.entity.MailService;
import com.ynu.entity.User;
import com.ynu.mapper.UserMapper;
import com.ynu.service.IUserService;
import com.ynu.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

/** 处理用户相关请求的控制器类 */
@Controller
@RequestMapping("/web")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/findAll")
    public String showNewPage(Model model) {
        List<User> list = userService.findAllUser();
        System.out.println(list);
        model.addAttribute("list", list);
        return "user";
    }

    @GetMapping("/user/search")
    public String searchUser(String keyword, Model model) {
        System.out.println("keyword=" + keyword);
        // 调用相应的服务方法从数据库中查找用户
        List<User> userList = userService.findAllMatch(keyword);
        // 将查找到的用户列表添加到模型中，以便在视图中使用
        model.addAttribute("list", userList);
        System.out.println(userList);
        // 返回视图名称，用于刷新页面
        return "user"; // 假设视图名称为 "user"
    }

    @GetMapping("/user/deleteUser")
    public String deleteUser(@RequestParam("id") int id) {
        System.out.println(id);
        userMapper.deleteUserById(id);
        return "redirect:/web/user/findAll"; // 重定向到用户列表页面
    }

}

@RestController
@RequestMapping("/users")
class UserControllerRest extends BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private MailService mailService;

    @RequestMapping("reg")
    public JsonResult<Void> reg(User user) {
        // 调用业务对象执行注册
        userService.reg(user);
        // 返回
        return new JsonResult<Void>(OK);
    }

    /*
    @RequestMapping("reg")
    public JsonResult<Void> reg(User user) {
        // 创建返回值
        JsonResult<Void> result = new JsonResult<Void>();
        try {
            // 调用业务对象执行注册
            userService.reg(user);
            // 响应成功
            result.setState(200);
        } catch (UsernameDuplicateException e) {
            // 用户名被占用
            result.setState(4000);
            result.setMessage("用户名已经被占用");
        } catch (InsertException e) {
            // 插入数据异常
            result.setState(5000);
            result.setMessage("注册失败，请联系系统管理员");
        }
        return result;
    }
    */

    @RequestMapping("login")
    public JsonResult<User> login(String username, String password, HttpSession session) {
        // 调用业务对象的方法执行登录，并获取返回值
        User data = userService.login(username, password);
        //登录成功后，将uid和username存入到HttpSession中
        session.setAttribute("uid", data.getUid());
        session.setAttribute("username", data.getUsername());
         System.out.println("Session中的uid=" + getUidFromSession(session));
         System.out.println("Session中的username=" + getUsernameFromSession(session));

        // 将以上返回值和状态码OK封装到响应结果中并返回
        return new JsonResult<User>(OK, data);
    }

    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword, String newPassword, HttpSession session) {
        // 调用session.getAttribute("")获取uid和username
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);

        // 调用业务对象执行修改密码
        userService.changePassword(uid, username, oldPassword, newPassword);
        // 返回成功
        return new JsonResult<Void>(OK);
    }

    @RequestMapping("reset_password")
    public JsonResult<Void> resetPassword(@RequestParam("id") Integer id) {
        System.out.println("userId=" + id);
        // 调用业务对象执行重置密码
        userService.resetPassword(id, "1234abcd,.");

        // 返回成功
        return new JsonResult<>(OK);
    }


    @GetMapping("get_by_uid")
    public JsonResult<User> getByUid(HttpSession session) {
        // 从HttpSession对象中获取uid
        Integer uid = getUidFromSession(session);
        // 调用业务对象执行获取数据
        User data = userService.getByUid(uid);

        // 响应成功和数据
        return new JsonResult<User>(OK, data);
    }

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(User user, HttpSession session) {
        // 从HttpSession对象中获取uid和username
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        // 调用业务对象执行修改用户资料
        userService.changeInfo(uid, username, user);
        // 响应成功
        return new JsonResult<Void>(OK);
    }

    /** 头像文件大小的上限值(10MB) */
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;
    /** 允许上传的头像的文件类型 */
    public static final List<String> AVATAR_TYPES = new ArrayList<String>();

    /** 初始化允许上传的头像的文件类型 */
    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");
    }

    @PostMapping("change_avatar")
    public JsonResult<String> changeAvatar(@RequestParam("file") MultipartFile file, HttpSession session) throws FileSizeException {
        // 判断上传的文件是否为空
        if (file.isEmpty()) {
            // 是：抛出异常
            throw new FileEmptyException("上传的头像文件不允许为空");
        }

        // 判断上传的文件大小是否超出限制值
        if (file.getSize() > AVATAR_MAX_SIZE) { // getSize()：返回文件的大小，以字节为单位
            // 是：抛出异常
            throw new FileSizeException("不允许上传超过" + (AVATAR_MAX_SIZE / 1024) + "KB的头像文件");
        }

        // 判断上传的文件类型是否超出限制
        String contentType = file.getContentType();
        // boolean contains(Object o)：当前列表若包含某元素，返回结果为true；若不包含该元素，返回结果为false
        if (!AVATAR_TYPES.contains(contentType)) {
            // 是：抛出异常
            throw new FileTypeException("不支持使用该类型的文件作为头像，允许的文件类型：" + AVATAR_TYPES);
        }

        // 获取当前项目的绝对磁盘路径
        String parent = session.getServletContext().getRealPath("upload");
        System.out.println(parent);
        // 保存头像文件的文件夹
        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存的头像文件的文件名
        String suffix = "";
        String originalFilename = file.getOriginalFilename();
        int beginIndex = originalFilename.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        String filename = UUID.randomUUID().toString() + suffix;

        // 创建文件对象，表示保存的头像文件
        File dest = new File(dir, filename);
        // 执行保存头像文件
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            // 抛出异常
            throw new FileStateException("文件状态异常，可能文件已被移动或删除");
        } catch (IOException e) {
            // 抛出异常
            throw new FileUploadIOException("上传文件时读写错误，请稍后重新尝试");
        }

        // 头像路径
        String avatar = "/upload/" + filename;
        // 从Session中获取uid和username
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        // 将头像写入到数据库中
        userService.changeAvatar(uid, username, avatar);

        // 返回成功头像路径
        return new JsonResult<String>(OK, avatar);
    }

    @RequestMapping("sendMail")
    public JsonResult<Void> sendMail(String toMail) {
        System.out.println("toMail=" + toMail);

        // 邮箱格式验证正则表达式
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        // 验证邮箱格式
        boolean isValidEmail = Pattern.matches(emailRegex, toMail);
        if (isValidEmail) {
            //随机生成验证码
            String code = String.valueOf(new Random().nextInt(899999) + 100000);
            mailService.send(toMail, "发送验证码", code);
            return new JsonResult<Void>(OK);
        } else {
            // 非法邮箱，返回错误信息
            return new JsonResult<Void>(404);
        }

    }
}