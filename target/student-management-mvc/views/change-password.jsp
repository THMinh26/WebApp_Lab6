<%-- Document : student-form Created on : Nov 15, 2025, 9:06:55 AM Author :
May01 --%> <%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>
      <c:choose> <c:when test="${(student != null && valid != false)}">Edit
      Student</c:when> <c:otherwise>Add New Student</c:otherwise> </c:choose>
    </title>
    <style>
      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }

      body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 20px;
      }

      .container {
        background: white;
        border-radius: 10px;
        padding: 40px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
        width: 100%;
        max-width: 600px;
      }

      h1 {
        color: #333;
        margin-bottom: 30px;
        font-size: 28px;
        text-align: center;
      }

      .form-group {
        margin-bottom: 25px;
      }

      label {
        display: block;
        margin-bottom: 8px;
        color: #555;
        font-weight: 500;
        font-size: 14px;
      }

      input[type='password'],
      input[type='email'],
      select {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #ddd;
        border-radius: 5px;
        font-size: 14px;
        transition: border-color 0.3s;
      }

      input:focus,
      select:focus {
        outline: none;
        border-color: #667eea;
      }

      .message {
        padding: 15px;
        margin-bottom: 20px;
        border-radius: 5px;
        font-weight: 500;
      }

      .success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
      }

      .error {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
      }

      .required {
        color: #dc3545;
      }

      .button-group {
        display: flex;
        gap: 15px;
        margin-top: 30px;
      }

      .btn {
        flex: 1;
        padding: 14px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
        text-decoration: none;
        text-align: center;
        display: inline-block;
      }

      .btn-primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
      }

      .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
      }

      .btn-secondary {
        background-color: #6c757d;
        color: white;
      }

      .btn-secondary:hover {
        background-color: #5a6268;
      }

      .info-text {
        font-size: 12px;
        color: #666;
        margin-top: 5px;
      }

      .error {
        color: red;
        font-size: 14px;
        display: block;
        margin-top: 5px;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <h1>✏️ Change Password</h1>
      <c:if test="${not empty error}">
        <span class="message error">${error}</span>
      </c:if>

      <form action="change-password" method="post">
        <div class="form-group">
          <label for="">
            Current Password <span class="required">*</span>
          </label>
          <input
            type="password"
            name="currentPassword"
            placeholder="Current Password"
          />
          <c:if test="${not empty errorCurrent}">
            <span class="error">${errorCurrent}</span>
          </c:if>
        </div>

        <div class="form-group">
          <label for=""> New Password <span class="required">*</span> </label>
          <input
            type="password"
            name="newPassword"
            placeholder="New Password"
          />
          <c:if test="${not empty errorNew}">
            <span class="error">${errorNew}</span>
          </c:if>
        </div>

        <div class="form-group">
          <label for="">
            Confirm Password <span class="required">*</span>
          </label>
          <input
            type="password"
            name="confirmPassword"
            placeholder="Confirm Password"
          />
          <c:if test="${not empty errorConfirm}">
            <span class="error">${errorConfirm}</span>
          </c:if>
        </div>

        <!-- Buttons -->
        <div class="button-group">
          <button type="submit" class="btn btn-primary">Change Password</button>
          <a href="student?action=list" class="btn btn-secondary">
            ❌ Cancel
          </a>
        </div>
      </form>
    </div>
  </body>
</html>
