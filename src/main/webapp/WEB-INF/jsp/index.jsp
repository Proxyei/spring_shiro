<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
</head>
<body>

	<form action="userLogin" method="post">
		<div>
			<label>username: <input type="text" name="username" />
			</label>
		</div>
		<br />

		<div>
			<label>password: <input type="password" name="password" />
			</label>
		</div>
		<div>
			<input type="submit" name="submit" value="submit" />
		</div>

	</form>

</body>
</html>