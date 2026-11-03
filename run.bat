@echo off
title Quan Ly sinh vien - Java Swing
cd /d "%~dp0dist"
java -jar Baitaplon-JavaSwing.jar
if %errorlevel% neq 0 (
    echo.
    echo Co loi xay ra khi chay chuong trinh!
    pause
)
