//存放要交互的js逻辑代码
//js很容一些乱,所以要把js模块化,不要把js写成一坨,可读性差
//java的包层次很清晰,
// js没有包的概念,但是js有1个json表示对象的方式
//seckill.detail.init(params)---把这一串想象成: seckill是包名,detail是类名,init()是方法名
//通过这中语法,我们也能让js模拟出高级语言封包的概念
var seckill = {
    // 封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    }
    ,
    //验证手机号
    validatePhone:

        function (phone) {
            //判断phone是否为空,空的话就是undefine,undifine的话就是fasle
            if (phone && phone.length == 11 && !isNaN(phone)) {
                return true;
            } else {
                return false;
            }
        }

    ,
    handleSeckillkill: function (seckillId, node) {
        //获取秒杀地址,控制显示逻辑, 执行秒杀
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            //在回调函数中,执行交互流程
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killURl:" + killUrl);
                    //给秒杀按钮绑定事件
                    //绑定一次点击事件
                    //为什么使用one,而不用click呢,因为one只绑定1次
                    //防止用户连续点击,造成同一时间大量的请求,对服务器的处理和压力都有影响
                    $('#killBtn').one('click', function () {
                        //绑定执行秒杀请求的操作
                        //1.先禁用按钮
                        $(this).addClass('disable');
                        //2.发送秒杀请求执行秒杀
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //3:显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                                console.log("killResult"+result);
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开启秒杀
                    //什么情况下会出现这种情况呢,各种客户端,运行时间足够长,计时存在差异,
                    //而服务器通过NTP同步网络时间,
                    //从而客户端认为已经可以秒杀,但是服务器的秒杀时间还没到
                    //然后告诉用户计时太快
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //再走一遍countdown()的逻辑
                    seckill.countdown(seckillId, now, stat, end);

                }
            } else {
                console.log('result:' + result);
            }
        });
    }
    ,
//为什么要封装成这个函数呢:
//(1)init函数已经非常的长
//(2)countdown函数会重用
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var timeBox = $('#seckill-box');
        //时间判断
        if (nowTime > endTime) {
            //秒杀结束
            timeBox.html('秒杀结束!');
        } else if (nowTime < startTime) {
            //秒杀未开始,计时事件绑定
            var killTime = new Date(startTime + 1000);//加上1秒的作用,是防止客户端计时的一个偏移,不加也可以
            timeBox.countdown(killTime, function (event) {//每一次时间变化都会回调这个函数
                //时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                timeBox.html(format);
                //时间完成后回调事件
            }).
            on('finish.countdown', function () {
                //获取秒杀地址,控制显示逻辑,执行秒杀
                //倒计时完成后出现按钮,用户点击的时候就可以执行秒杀了
                seckill.handleSeckillkill(seckillId, timeBox);
            });
        } else {
            //秒杀开始
            seckill.handleSeckillkill(seckillId, timeBox);//向node中放置显示内容,比如秒杀按钮或"秒杀结束"等提示语       }
        }
    }
    ,
    // 详情页秒杀逻辑
    detail: {
        // 详情页面初始化
        init: function (params) {
            //手机验证和登录,计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //验证手机号
            //也就是验证登录操作
            //验证建议提取到更上层的逻辑
            //为什么放在上层呢,因为有可能有多个地方都需要验证
            if (!seckill.validatePhone(killPhone)) {
                //绑定phone的弹出框
                //控制输出
                //这个节点已经不是一个简单的div了,因为它是一个model
                //是bootstrap的一个组件,有自己的方法
                var killPhoneModal = $('#killPhoneModal');
                //方法叫model,传入1个json
                //显示了弹出层
                killPhoneModal.modal({
                    show: true,//显示弹出层
                    backdrop: false,//禁止位置关闭(点击其他位置关闭弹出层)
                    keyboard: false//关闭键盘事件(禁止ESC等关闭弹出层)
                });
                $('#killPhoneModal').css("z-index", "111");
                $('#killPhoneBtn').click(function () {//用户点击了这个按钮,就认为用户已经输入了手机号
                    var inputPhone = $('#killPhoneKey').val();
                    //我们再做一次验证
                    if (seckill.validatePhone(inputPhone)) {
                        //刷新页面之前,电话写入cookie
                        //为什么不给全路径呢?给一些不需要cookie的url传入cookie,对服务器的处理量和http的字节量都会增加
                        //把cookie限定在秒杀模块
                        $.cookie('killPhone', inputPhone, {expire: 7, path: '/seckill'})//
                        //验证通过,刷新页面
                        window.location.reload();//刷新完以后,会重新走一遍逻辑,会刷新我们的cookie
                    } else {
                        //如果用户输入的手机号有问题,
                        //怎么办呢,我们在页面中依然埋了个节点
                        //这里的提示信息写的有点粗糙,应该是有一个前端字典
                        //先隐藏,为什么现隐藏?因为put内容的时候,可能会有1个效果,我们不希望用户看到,所以先隐藏
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });//函数本身也是个对象,因为js是个动态语言
            }
            //已经登录
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function (result) {//我们不希望代码里随处放置跟后端通信的url,所以把url统一封装起来
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //时间判断,
                    //判断代码封装到函数中,就叫countdown()
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                }
            }, 'json');
        }
    }
}