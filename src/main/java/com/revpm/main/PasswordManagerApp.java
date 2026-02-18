package com.revpm.main;
import com.revpm.dao.PasswordVaultDao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revpm.model.PasswordEntry;
import com.revpm.dao.SecurityQuestionDao;
import com.revpm.model.SecurityQuestion;

import com.revpm.util.PasswordGenerator;
import com.revpm.util.PasswordUtil;
import com.revpm.dao.UserDAO;
import com.revpm.model.User;

import java.util.List;
import java.util.Scanner;
import com.revpm.util.OtpUtil;


public class PasswordManagerApp {
	
	 private static final Logger logger =
	            LogManager.getLogger(PasswordManagerApp.class);
    public static void main(String[] args) {
    	 logger.info("\nPassword Manager Application Started");
    	
        Scanner sc = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();

        while (true) {
        	System.out.println("--Password Manager--");
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
           
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            
            //taking user input as string
            String option = sc.nextLine();
            
            if (!option.matches("\\d+")) {
                System.out.println("❌ Please enter a valid number option!");
                continue; // goes back to menu
            }

            int choice = Integer.parseInt(option);

            //sc.nextLine(); // consume newline

            switch (choice) {
            
            case 1:
                User user = new User();
                SecurityQuestionDao securityDAO = new SecurityQuestionDao();
                String username, fullName, email, password;
                
                boolean exitRegistration = false; // flag to check if user wants to exit

                // ---------- USERNAME & FULL NAME ----------
                System.out.print("Enter username: ");
                username = sc.nextLine().trim();
                user.setUsername(username);

                System.out.print("Enter full name: ");
                fullName = sc.nextLine().trim();
                user.setFullName(fullName);

                // ---------- EMAIL INPUT LOOP ----------
                while (true) {
                    System.out.print("Enter email: ");
                    email = sc.nextLine().trim();

                    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                        System.out.println("❌ Enter a valid email address.");
                        continue;
                    }

                    if (!userDAO.isEmailAvailable(email)) {
                        System.out.println("❌ This email is already registered.");
                        System.out.println("1. Enter a different email");
                        System.out.println("2. Exit registration");
                        System.out.print("Choose: ");
                        String choice1 = sc.nextLine().trim();
                        


                        if (choice1.equals("1")) {
                            continue; // re-enter email
                        } else {
                            System.out.println("Exiting registration. Please use the main menu.");
                            exitRegistration = true; // mark to exit
                            break;
                        }
                    }

                    // Email is valid & unique
                    user.setEmail(email);
                    break;
                }

                if (exitRegistration) break; // exit case-1 completely → back to main menu

                // ---------- PASSWORD INPUT LOOP ----------
                while (true) {
                    System.out.print("Do you want a system-generated strong password? (yes/no): ");
                    String choice1 = sc.nextLine().trim();

                    if (choice1.equalsIgnoreCase("yes")) {
                        password = PasswordGenerator.generateStrongPassword();
                        System.out.println("🔐 Suggested Password: " + password);
                        System.out.println("⚠️ Please save this password securely.");
                        break;
                    } else {
                        System.out.print("Enter your password: ");
                        password = sc.nextLine().trim();

                        if (password.length() >= 8 &&
                            password.matches(".*[A-Z].*") &&
                            password.matches(".*[a-z].*") &&
                            password.matches(".*[0-9].*") &&
                            password.matches(".*[^A-Za-z0-9].*")) {
                            break;
                        } else {
                            System.out.println("❌ Password must be at least 8 characters and contain uppercase, lowercase, number & special character.");
                        }
                    }
                }

                user.setMasterPassword(password);

                // ---------- INSERT USER ----------
                boolean success = userDAO.registerUser(user);

                if (success) {
                    // ---------- SECURITY QUESTION & ANSWER ----------
                    String securityQuestion = "", securityAnswer = "";
                    while (true) {
                        System.out.print("Enter your security question: ");
                        securityQuestion = sc.nextLine().trim();

                        System.out.print("Enter your answer: ");
                        securityAnswer = sc.nextLine().trim();

                        if (securityQuestion.isEmpty() || securityAnswer.isEmpty()) {
                            System.out.println("❌ Security question and answer cannot be empty.");
                            continue;
                        }
                        break;
                    }

                    SecurityQuestion sq = new SecurityQuestion();
                    sq.setUserId(user.getUserId());
                    sq.setQuestion(securityQuestion);
                    sq.setAnswer(securityAnswer);

                    boolean sqSaved = securityDAO.saveQuestion(sq);
                    if (sqSaved) {
                        System.out.println("✅ User registered successfully!");
                    } else {
                        System.out.println("❌ User registered but failed to save security question.");
                    }

                } else {
                    System.out.println("❌ Registration failed.");
                }

                break;


        
                case 2:
                    System.out.print("Email: ");
                    String mail = sc.nextLine();

                    System.out.print("Password: ");
                    String pwd = sc.nextLine();

                    User loggedUser = userDAO.login(mail, pwd);

                    if (loggedUser != null) {
                        System.out.println("Login successful! Welcome " + loggedUser.getFullName());

                        PasswordVaultDao vaultDao = new PasswordVaultDao();
                        boolean loggedIn = true;

                        while (loggedIn) {
                            System.out.println("\n--- Password Vault ---");
                            System.out.println("1. Add Password");
                            System.out.println("2. View My Password");
                            System.out.println("3. Search Password");
                            System.out.println("4. Update Password");
                            System.out.println("5. Delete Password");
                            System.out.println("6. Logout");
                            System.out.println("7. Update Profile");

                            System.out.print("Choose: ");
                            //int option = sc.nextInt();
                            String input = sc.nextLine();

                            if (!input.matches("\\d+")) {
                                System.out.println("❌ Please enter a valid number option!");
                                continue; // goes back to menu
                            }

                            int option1 = Integer.parseInt(input);

                            //sc.nextLine(); // consume newline

                            switch (option1) {

                                case 1:
                                    PasswordEntry entry = new PasswordEntry();
                                    entry.setUserId(loggedUser.getUserId());

                                    System.out.print("Account Name: ");
                                    entry.setAccountName(sc.nextLine());

                                    System.out.print("Account Username: ");
                                    entry.setAccountUsername(sc.nextLine());

                                    System.out.print("Account Password: ");
                                    String accPwd = sc.nextLine();

                                    // 🔐 ENCRYPT ACCOUNT PASSWORD
                                    entry.setAccountPassword(PasswordUtil.encrypt(accPwd));

                                    vaultDao.addPassword(entry);
                                    System.out.println("Password saved successfully!");
                                    break;

                                case 2:
                                    System.out.print("Enter master password to continue: ");
                                    String masterPwd = sc.nextLine();

                                    if (!userDAO.verifyMasterPassword(
                                            loggedUser.getUserId(), masterPwd)) {
                                        System.out.println("Incorrect master password!");
                                        break;
                                    }

                                    List<PasswordEntry> list =
                                            vaultDao.getPasswordsByUser(loggedUser.getUserId());

                                    if (list.isEmpty()) {
                                        System.out.println("No passwords found.");
                                    } else {
                                        System.out.println("\nMy Passwords:");
                                        for (PasswordEntry p : list) {
                                            System.out.println(
                                                    p.getAccountName() + " | " +
                                                    p.getAccountUsername() + " | " +
                                                    PasswordUtil.decrypt(p.getAccountPassword())
                                            );
                                        }
                                    }
                                    break;

                                case 3:
                                    System.out.print("Enter Account Name: ");
                                    String accName = sc.nextLine();

                                    PasswordEntry p =
                                            vaultDao.searchByAccountName(loggedUser.getUserId(), accName);

                                    if (p != null) {
                                        System.out.println(
                                                p.getAccountName() + " | " +
                                                p.getAccountUsername() + " | " +
                                                PasswordUtil.decrypt(p.getAccountPassword())
                                        );
                                    } else {
                                        System.out.println("No password found for this account.");
                                    }
                                    break;

                                case 4:
                                    System.out.print("Enter Account Name: ");
                                    String updateAccName = sc.nextLine();

                                    System.out.print("Enter New Password: ");
                                    String newPwd = sc.nextLine();

                                    String encryptedPwd = PasswordUtil.encrypt(newPwd);

                                    if (vaultDao.updatePassword(
                                            loggedUser.getUserId(), updateAccName, encryptedPwd)) {
                                        System.out.println("Password updated successfully!");
                                    } else {
                                        System.out.println("Account not found!");
                                    }
                                    break;

                                case 5:
                                    System.out.print("Enter Account Name to Delete: ");
                                    String delAccName = sc.nextLine();

                                    System.out.print("Are you sure? (Y/N): ");
                                    String confirm = sc.nextLine();

                                    if (confirm.equalsIgnoreCase("Y")) {
                                        if (vaultDao.deletePassword(
                                                loggedUser.getUserId(), delAccName)) {
                                            System.out.println("Password deleted successfully!");
                                        } else {
                                            System.out.println("Account not found!");
                                        }
                                    } else {
                                        System.out.println("Delete cancelled.");
                                    }
                                    break;

                                case 6:
                                    loggedIn = false;
                                    System.out.println("Logged out.");
                                    break;

                                case 7:
                                    System.out.print("Enter new full name: ");
                                    String newName = sc.nextLine();

                                    System.out.print("Enter new email: ");
                                    String newEmail = sc.nextLine();

                                    if (userDAO.updateUserProfile(
                                            loggedUser.getUserId(), newName, newEmail)) {

                                        loggedUser.setFullName(newName);
                                        loggedUser.setEmail(newEmail);

                                        System.out.println("Profile updated successfully!");
                                    } else {
                                        System.out.println("Profile update failed.");
                                    }
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Invalid email or password.");
                    }
                    break;

               
                case 3:
                    System.out.print("Enter your email: ");
                    String emailFP = sc.nextLine();

                    User userFP = userDAO.findByEmail(emailFP);

                    if (userFP == null) {
                        System.out.println("User not found!");
                        break;
                    }

                    System.out.println("\nReset password using:");
                    System.out.println("1. Security Question");
                    System.out.println("2. OTP");
                    System.out.print("Choose: ");

                    //int resetChoice = sc.nextInt();
                    String input = sc.nextLine();

                    if (!input.matches("\\d+")) {
                        System.out.println("❌ Please enter a valid number option!");
                        continue; // goes back to menu
                    }

                    int resetChoice = Integer.parseInt(input);

                    //sc.nextLine(); // consume newline

                    boolean verified = false;

                    // ===== OPTION 1 : SECURITY QUESTION =====
                    if (resetChoice == 1) {

                        SecurityQuestionDao sqDao = new SecurityQuestionDao();
                        SecurityQuestion sq = sqDao.getQuestionByUserId(userFP.getUserId());

                        if (sq == null) {
                            System.out.println("Security question not set!");
                            break;
                        }

                        System.out.println("Security Question:");
                        System.out.println(sq.getQuestion());

                        System.out.print("Your Answer: ");
                        String ans = sc.nextLine();

                        if (ans.equalsIgnoreCase(sq.getAnswer())) {
                            verified = true;
                        } else {
                            System.out.println("Wrong answer!");
                            break;
                        }
                    }

                    // ===== OPTION 2 : OTP =====
                    else if (resetChoice == 2) {

                        String otp = OtpUtil.generateOtp();

                        System.out.println("🔐 OTP sent to your email (demo): " + otp);

                        System.out.print("Enter OTP: ");
                        String enteredOtp = sc.nextLine();

                        if (otp.equals(enteredOtp)) {
                            verified = true;
                        } else {
                            System.out.println("Invalid OTP!");
                            break;
                        }
                    }

                    else {
                        System.out.println("Invalid choice!");
                        break;
                    }

                    // ===== RESET PASSWORD =====
                    if (verified) {

                        System.out.print("Enter new password: ");
                        String newPwd = sc.nextLine();

                        System.out.print("Confirm new password: ");
                        String confirmPwd = sc.nextLine();

                        if (!newPwd.equals(confirmPwd)) {
                            System.out.println("Passwords do not match!");
                            break;
                        }

                        // ✅ DO NOT HASH HERE
                        if (userDAO.updatePassword(userFP.getUserId(), newPwd)) {
                            System.out.println("Password reset successful! Please login.");
                        } else {
                            System.out.println("Password reset failed!");
                        }
                    }
                    break;



                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
            }
        }
    }
}
