# 煎饼计划 Java 组进阶任务（二）
## 使用 Postman 模拟注册请求
在请求类型中选择 POST，然后在请求 URL 输入框中输入 http://localhost:8086/attendance/api/authentication/register，
并在 "Body" 部分输入你的注册信息，格式为 JSON，例如：

```json
{
  "username": "test",
  "password": "test",
  "email": "test@example.com",
  "qq": "12345678",
  "confirmPassword": "test"
}
```

然后点击 "Send" 按钮。