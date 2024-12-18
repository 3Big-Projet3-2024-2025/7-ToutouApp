<#import "email.ftl" as layout>
<@layout.email>
    <p>Hello,</p>
    <p>To verify your email address, please click the button below:</p>
    <a href="${link}" class="button">Verify Email</a>
    <p>If you did not request this, please ignore this email.</p>
</@layout.email>
