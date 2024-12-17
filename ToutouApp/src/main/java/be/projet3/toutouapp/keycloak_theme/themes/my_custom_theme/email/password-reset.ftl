<#import "email.ftl" as layout>
<@layout.email>
    <p>Hello,</p>
    <p>We received a request to reset your password. Click the button below to proceed:</p>
    <p style="text-align: center;">
        <a href="${link}" class="button">Reset Password</a>
    </p>
    <p>If you didn't request a password reset, you can safely ignore this email.</p>
</@layout.email>
