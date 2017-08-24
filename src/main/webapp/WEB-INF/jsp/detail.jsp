<%--
  Created by IntelliJ IDEA.
  User: 兴
  Date: 2017/8/23
  Time: 23:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀详情页</title>
    <%@include file="common/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="pannel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <%--显示timer图标--%>
                <span class="glyphicon glyphicon-time"></span>
                <%--展示倒计时--%>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>

</div>
<%--登录弹出层,输入电话--%>
<%--我们要通过前端把秒杀系统登录做完,正常是在后端做的,因为登录不是我们这个系统的重点
所以我们用了这么个前端的技巧--%>
<%--modal代表bootstrap的弹出框--%>
<div id="killPhoneModal"  class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="model-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>秒杀电话:
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey"
                               placeholder="填写手机号^o^" class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <%--验证信息--%>
                <span id="killPhoneMessage" class="glyphicon"></span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>
            </div>
        </div>
    </div>
</div>
</body>

<%--这里使用了很多的cdn来加载js--%>
<%--一个稳定快速的cdn往往比部署在服务器上更有效,适合高并发的网站--%>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.bootcss.com/jquery/2.0.0/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<%--jquery cookie 操作插件--%>
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<%--<script src="<%=request.getContextPath() %>/webjars/jquery.countdown/2.1.0/dist/jquery.countdown.min.js"></script>--%>
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<%--开始编写交互逻辑--%>
<%--有个小坑,script不要使用/>,否则下面的js是不加载的--%>
<script src="/resources/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        //使用EL表达式传入参数
        //使用EL表达式把我们需要的数据传给js,这也是项目中常用的技巧
        //建议除了init的调用,其他js代码都放到js文件中
        seckill.detail.init({
            seckillId:${seckill.seckillId},
            startTime:${seckill.startTime.time},//毫秒,转换成毫秒方便js直接做解析,直接识别毫秒的时间表示方式
            endTime:${seckill.endTime.time}
        });
    });
</script>
</html>