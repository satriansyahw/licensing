@echo off
echo ===================================================
echo Git Commit and Push Automation Script
echo ===================================================

cd /d "%~dp0"

:: 1. Check if Git is installed
where git >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Git is not installed or not added to your PATH environment variable.
    echo Please install Git and try again.
    pause
    exit /b 1
)

:: 2. Initialize repository if not already initialized
if not exist ".git" (
    echo [INFO] Initializing Git repository...
    git init
) else (
    echo [INFO] Git repository already initialized.
)

:: 3. Configure Remote URL
echo [INFO] Configuring remote origin...
git remote remove origin >nul 2>nul
git remote add origin https://github.com/satriansyahw/licensing

:: 4. Add files (respecting .gitignore)
echo [INFO] Adding files to staging...
git add .

:: 5. Commit changes
echo [INFO] Committing changes...
git commit -m "feat: add rate limiting, circuit breaker, and walkthrough documentation"

:: 6. Rename branch to main
echo [INFO] Setting branch name to main...
git branch -M main

:: 7. Push to GitHub
echo [INFO] Pushing to GitHub...
echo Note: If this is the first push, you might be prompted to log in to GitHub.
git push -u origin main

echo ===================================================
if %errorlevel% eq 0 (
    echo [SUCCESS] Commit and Push completed successfully!
) else (
    echo [WARNING] There was an issue during git push. Please check the logs above.
)
echo ===================================================
pause
