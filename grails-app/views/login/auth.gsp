<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>OOICI - LCA Demo</title>
<link rel="stylesheet" type="text/css" href="/css/reset.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/css/text.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/css/960.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/css/nav.css" media="screen" />
<!--[if IE 6]><link rel="stylesheet" type="text/css" href="../../../css/ie6.css" media="screen" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" type="text/css" href="../../../css/ie.css" media="screen" /><![endif]-->
</head>
<body>
<div class="container_16">
  <div class="grid_16">
    <h1 id="branding"> <a href="../../../">Ocean Observatories Initiative Cyberinfrastructure</a> </h1>
  </div>
  <div class="clear"></div>
  <div class="grid_16">
  </div>
  <div class="clear"></div>
  <div class="grid_16">
    <h2 id="page-heading">LCA Demo</h2>
  </div>
  <div class="clear"></div>
  <div class="grid_4">
    <div class="box">
      <h2>Welcome</h2>
      <div class="block" id="paragraphs">
        <p>To login as administrator: <strong>admin/admin</strong></p>
        <p>To login as a user: <strong>test/test</strong></p>
      </div>
    </div>
  </div>
  <div class="grid_7">
  <div class="box">
      <h2>Welcome</h2>
        <g:if test='${flash.message}'>
        <div class='login_message'>${flash.message}</div>
        </g:if>
      <div class="block" id="login-forms">
        <form id='loginForm' action='${postUrl}' method='POST'>
          <fieldset class="login">
            <legend>Login</legend>
            <p class="notice">Please login.</p>
            <p>
              <label>Username: </label>
              <input type='text' name='j_username' id='username' value="admin"/>
            </p>
            <p>
              <label>Password: </label>
              <input type="password" name="j_password" id='password' value="admin"/>
            </p>
            <p>
                <label for='remember_me'>Remember me</label>
                <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
                <g:if test='${hasCookie}'>checked='checked'</g:if> />
            </p>
            <input class="login button" type="submit" value="Login" />
          </fieldset>
        </form>
        <form action="">
          <fieldset>
            <legend>Register</legend>
            <p>If you do not already have an account, please create a new account to register.</p>
            <input type="submit" value="Create Account" />
          </fieldset>
        </form>
      </div>
    </div>
  </div>
  <div class="grid_5">

  </div>
  <div class="clear"></div>
  <div class="grid_16" id="site_info">
    <div class="box">
      <p>OOICI 2010 - LCA</p>
    </div>
  </div>
  <div class="clear"></div>
</div>
<script type="text/javascript" src="/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript" src="/js/jquery-fluid16.js"></script>
<script type='text/javascript'>
<!--
(function(){
	document.forms['loginForm'].elements['j_username'].focus();
})();
// -->
</script>
</body>
</html>