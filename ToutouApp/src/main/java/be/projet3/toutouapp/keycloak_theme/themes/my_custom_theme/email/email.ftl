<!DOCTYPE html>
<html>
<head>
    <title>${realmName} - Notification</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
            color: #333333;
        }
        .container {
            width: 100%;
            max-width: 600px;
            margin: 20px auto;
            background-color: #ffffff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .header {
            background-color: #FF8C00;
            color: white;
            text-align: center;
            padding: 15px;
            font-size: 20px;
            font-weight: bold;
        }
        .content {
            padding: 20px;
            line-height: 1.6;
            text-align: center;
        }
        .footer {
            padding: 10px;
            font-size: 12px;
            color: #666666;
            background-color: #f0f0f0;
            text-align: center;
        }
        .button {
            display: inline-block;
            margin: 20px auto;
            padding: 10px 20px;
            background-color: #FF8C00;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">${realmName}</div>
    <div class="content">
        <#nested>
    </div>
    <div class="footer">
        <p>This email was sent by ${realmName}. If you didn't request this, please ignore it.</p>
    </div>
</div>
</body>
</html>
