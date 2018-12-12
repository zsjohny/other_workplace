<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>管理员上传资料</title>
<script type="text/javascript">
	
</script>
</head>
<body>


	<div class="login">
		<div class="box png">
			<div class="logo png"></div>
			<form name="admin" action="/ouliao/j_spring_security_check" method="post">
				<div style="text-align: center">
					<span style="color: red; font-size: 12px;" id="openCookie"></span>
				</div>
				<div class="input">
					<div class="log">
						<div class="name">
							<label>用户名</label><input type="text"
								 class="text" id="value_1"
								placeholder="用户名" name="j_username" tabindex="1">
						</div>
						<div class="pwd">
							<label>密 码</label><input type="password" 
								class="text" id="value_2" placeholder="密码" name="j_password"
								tabindex="2"> <input type="submit" class="submit"
								tabindex="3" value="登录">

							<div style="text-align: center">
								<span style="color: red; font-size: 14px;" id="adminErrorMsg"></span>
							</div>
							<div class="check"></div>
						</div>
						<div class="tip"></div>
					</div>
				</div>
			</form>
		</div>
		<div class="air-balloon ab-1 png"></div>
		<div class="air-balloon ab-2 png"></div>
		<div class="footer"></div>
	</div>

	<script type="text/javascript" src="js/jQuery.js"></script>
	<script type="text/javascript" src="js/fun.base.js"></script>
	<script type="text/javascript" src="js/script.js"></script>
</body>
</html>