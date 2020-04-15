<!DOCTYPE html>
<%@include file="include/view-lib.jsp"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <!--- Basic Page Needs  -->
    <meta charset="utf-8">
    <title>CQI Service</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Mobile Specific Meta  -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value='/resources/new/css/style.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/new/css/responsive.css'/>">
    <!-- Favicon -->
    <link rel="shortcut icon" type="image/png" href="<c:url value='/resources/img/favicon.png'/>">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
</head>

<body>
    <!-- prealoader area start -->
    <div id="preloader">
        <div class="spiner"></div>
    </div>
    <!-- prealoader area end -->
    <!-- Crumbs area start -->
    <div class="crumbs-area">
        <div class="container">
            <div class="row">
                <div class="col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1 col-xs-12">
                    <div class="crumbs-header">
                        <h2 class="cd-headline letters rotate-3">
                            <span class="cd-words-wrapper">
                                <b class="is-visible">CQI&nbspService</b>
                                <b>CQI&nbspService</b>
                            </span>
                        </h2>
                        <p>歡迎回來 <br/>請選擇您想前往的服務!</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Crumbs area end -->
    <!-- counter-box area start -->
    <div class="counter-box">
        <div class="container">
            <div class="row">
                <div class="counter-box-container">
                    <div class="col-md-3 col-sm-4 col-xs-12 col-2">
                        <div class="counter-item">
                            <b class="counter-up">8</b>
                            <span>Total</span>
                        </div>
                    </div>
                    <div class="col-md-3 col-sm-4 col-xs-12 col-2">
                        <div class="counter-item">
                            <b class="counter-up">3</b>
                            <span>Services</span>
                        </div>
                    </div>
                    <div class="col-md-3 col-sm-4 col-xs-12 col-2">
                        <div class="counter-item">
                            <b class="counter-up">2</b>
                            <span>Official Website</span>
                        </div>
                    </div>
                    <div class="col-md-3 col-sm-4 col-xs-12 col-2">
                        <div class="counter-item">
                            <b class="counter-up">3</b>
                            <span>Video</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- counter-box area end -->
    <!-- demo area start -->
    <div class="demo-area">
        <div class="container">
            <div class="row">
                <div class="col-md-4 col-sm-4 col-xs-12 col-2">
                    <div class="demo-item">
                        <div class="thumb-area">
                            <a href="loginPage" target="_blank"><img src="<c:url value='/resources/new/img/demo-thumb/index.jpg'/>" alt="demo image"></a>
                            <a href="loginPage" class="lets-view" target="_blank"><i class="fa fa-long-arrow-right"></i></a>
                        </div>
                        <div class="demo-title">
                            <h2>CQI出勤系統</h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-4 col-xs-12 col-2">
                    <div class="demo-item">
                        <div class="thumb-area">
                            <a href="backstage" target="_blank"><img src="<c:url value='/resources/new/img/demo-thumb/index2.jpg'/>" alt="demo image"></a>
                            <a href="backstage" class="lets-view" target="_blank"><i class="fa fa-long-arrow-right"></i></a>
                        </div>
                        <div class="demo-title">
                            <h2>CQI出勤系統HR管理後台</h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-4 col-xs-12 col-2">
                    <div class="demo-item">
                        <div class="thumb-area">
                            <a href="http://officefeeback.cqiserv.com" target="_blank"><img src="<c:url value='/resources/new/img/demo-thumb/index3.jpg'/>" alt="demo image"></a>
                            <a href="http://officefeeback.cqiserv.com" class="lets-view" target="_blank"><i class="fa fa-long-arrow-right"></i></a>
                        </div>
                        <div class="demo-title">
                            <h2>碰碰蛇傳說監控系統</h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-4 col-xs-12 col-2">
                    <div class="demo-item">
                        <div class="thumb-area">
                            <a href="http://www.cqigames.com" target="_blank"><img src="<c:url value='/resources/new/img/demo-thumb/index4.jpg'/>" alt="demo image"></a>
                            <a href="http://www.cqigames.com" class="lets-view" target="_blank"><i class="fa fa-long-arrow-right"></i></a>
                        </div>
                        <div class="demo-title">
                            <h2>CQI官網</h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-4 col-xs-12 col-2">
                    <div class="demo-item">
                        <div class="thumb-area">
                            <a href="http://ponponsnake.cqigames.com" target="_blank"><img src="<c:url value='/resources/new/img/demo-thumb/index5.jpg'/>" alt="demo image"></a>
                            <a href="http://ponponsnake.cqigames.com" class="lets-view" target="_blank"><i class="fa fa-long-arrow-right"></i></a>
                        </div>
                        <div class="demo-title">
                            <h2>碰碰蛇傳說官網</h2>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- demo area end -->
    <!-- demo area start -->
    <div class="demo-area">
        <div class="container">
            <div class="row">
                <div class="col-md-4 col-sm-4 col-xs-12 col-2">
                    <div class="demo-item">
                        <div class="thumb-area">
                            <iframe width="100%" height="310px" src="https://www.youtube.com/embed/poePDGypYAc" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                        </div>
                        <div class="demo-title">
                            <h2>碰碰蛇傳說影片</h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-4 col-xs-12 col-2">
                    <div class="demo-item">
                        <div class="thumb-area">
                            <iframe width="100%" height="310px" src="https://www.youtube.com/embed/IsUH_mHCjoc" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                        </div>
                        <div class="demo-title">
                            <h2>碰碰蛇傳說影子宣傳影片</h2>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-4 col-xs-12 col-2">
                    <div class="demo-item">
                        <div class="thumb-area">
                            <iframe width="100%" height="310px" src="https://www.youtube.com/embed/yQ6cUpgg8dA" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                        </div>
                        <div class="demo-title">
                            <h2>碰碰蛇傳說Sky宣傳影片</h2>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- demo area end -->

    <!-- footer area start -->
    <footer>
        <div class="footer-area">
            <div class="container">
                <div class="footer-content text-center">
                    <p class="copy-right">
                    	<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
						Copyright &copy;2019 All rights reserved | This template is made with <i class="fa fa-heart-o" aria-hidden="true"></i> by <a href="https://colorlib.com" target="_blank">Colorlib</a>
						<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
					</p>
                </div>
            </div>
        </div>
    </footer>
    <!-- footer area end -->

    <!-- Scripts -->
    <script src="<c:url value='/resources/new/js/jquery-3.2.0.min.js'/>" type="text/javascript"></script>
    <script src="<c:url value='/resources/new/js/bootstrap.min.js'/>" type="text/javascript"></script>
    <script src="<c:url value='/resources/new/js/animatedheadline.js'/>" type="text/javascript"></script>
    <script src="<c:url value='/resources/new/js/counterup.js'/>" type="text/javascript"></script>
    <script src="<c:url value='/resources/new/js/jquery.waypoints.min.js'/>" type="text/javascript"></script>
    <script src="<c:url value='/resources/new/js/theme.js'/>" type="text/javascript"></script>
</body>

</html>