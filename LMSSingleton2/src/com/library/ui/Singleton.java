package com.library.ui;

import java.util.Scanner;

import com.library.book.BookFunc;

public class Singleton {

	BookFunc bo = BookFunc.getinstance();

	public static void main(String[] args) {
		Singleton ui = new Singleton();
		ui.welcome();

	}
	public void welcome() {
		System.out.println("library management system");
		loginBoundary();
	}

	public void loginBoundary() {
		System.out.print("Administrator account:");
		Scanner scanner = new Scanner(System.in);
		String username = scanner.nextLine();
		System.out.print("Login password:");
		Scanner scanner1 = new Scanner(System.in);
		String password = scanner1.nextLine();
		int flag = bo.login(username, password);
		switch (flag) {
		case 0:
			System.out.println("Wrong password, please re-enter.");
			loginBoundary();
			break;

		case 1:
			System.out.println("Login successfully!");
			showMenu();
			break;
		case -1:
			System.out.println("The administrator account you entered does not exist, please confirm and enter again.");
			loginBoundary();
			
			break;
		}
	}

	public void showMenu() {
		System.out.println("Please enter the function number to enter the corresponding function:");
		System.out.println("All book information-number: 1");
		System.out.println("Enter a new book-number: 2");
		System.out.println("Modify the basic information of the book-number: 3");
		System.out.println("Delete book function-number: 4");
		System.out.println("Book Return Function-Number: 5");
		System.out.println("Book Borrowing Function-Number: 6");
		System.out.println("Exit the system-number: 7");
		System.out.print("Please enter the function number:");
		Scanner scanner = new Scanner(System.in);
		int a = scanner.nextInt();
		switch (a) {
		case 1:
			showBook();
			returnMethod();
			break;
		case 2:
			addBook();
			break;
		case 3:
			modBookInfo();
			break;
		case 4:
			delete();
			break;
		case 5:
			returnBook();
			break;
		case 6:
			welcome();
			scanner.close();
			break;
		}
	}

	public void returnMethod() {
		System.out.print("Press ENTER to return to the function main menu:");
		Scanner scanner = new Scanner(System.in);
		String i = scanner.nextLine();
		showMenu();
	}

	public void showBook() {
		bo.showBook();
	}

	public void addBook() {
		System.out.print("Please enter the name of the book to be added:");//Book title
		Scanner scanner = new Scanner(System.in);
		String bookname = scanner.nextLine();
		System.out.print ( "Please enter" + bookname + "author:");//Author
		Scanner scanner1 = new Scanner(System.in);
		String author = scanner1.nextLine();
		//System.out.print("Please enter "+ bookname +" the publication date of "(format such as 2000-01-01):");//publication date
		Scanner scanner2 = new Scanner(System.in);
		String pubdate = scanner2.nextLine();
		System.out.print("Please enter" + bookname + "total number of pages (pages):");//total number of pages
		Scanner scanner3 = new Scanner(System.in);
		String sumpagination = scanner3.nextLine();
		int flag = bo.addBook(bookname, author, pubdate, sumpagination);
		//1 means success, 0 means the new book already exists in the library, -1 means the warehouse is full
		switch (flag) {
		case 1:
			System.out.println("new book" + bookname + "added successfully, the library currently has" + bo.remainSpace() + "locations for storing new books.");
			returnMethod();//The return function in the book add interface, you can choose to continue adding or return to the main menu
			break;
		case 0:
			System.out.println("Failed to add book," + bookname + "Already exist in this library!");
			returnMethod();//The return function in the book add interface, you can choose to continue adding or return to the main menu
			break;
		case -1:
			System.out.println("Adding failed, the library is full, please delete some books before adding them.");
			showMenu();
			break;
		}
	}

	//Modify book information
	public void modBookInfo() {
		System.out.print("Please enter the title of the book you want to operate:");
		Scanner scanner = new Scanner(System.in);
		String bookname = scanner.nextLine();
		//Find related book information based on the title
		int flag = bo.selectBook(bookname);
		switch (flag) {
		case 0:
			System.out.print("Related books are not found, enter 0 and press Enter to enter the function of this layer, and enter 1 and press Enter to return to the function main menu.");
			int a = scanner.nextInt();
			if (a == 0) {
				modBookInfo();
			} else if (a == 1) {

				showMenu();
			}
			break;

		case -1:
			System.out.println("The title of the book is empty, enter 0 and press Enter to enter the function of this layer, and enter 1 and press Enter to return to the function main menu.");
			int a1 = scanner.nextInt();
			if (a1 == 0) {
				modBookInfo();
			} else {
				showMenu();
			}
			break;

		case 1:
			//System.out.print("The above information is the result of searching related book titles, where (the number "1" represents the title of the book, the number "2" represents the author, the number "3" represents the publication date, and the number "4" represents the total number of pages , The number "5" represents the borrower) Enter the relevant number to modify the relevant information: ");
			int a2 = scanner.nextInt();//1 title 2 author 3 publication date 4 total pages 5 borrower
			if (a2> 0 && a2 <6) {
				System.out.print("Please enter what you want to modify" + sortName(a2) + ":");
				Scanner scanner2 = new Scanner(System.in);
				String value = scanner2.nextLine();
				int index = bo.selectIndex(bookname);
				bo.modinfo(a2, value, index);
				System.out.println(bookname + "The related information of this book has been modified successfully!");
				returnMethod();
			} else {
				System.out.println("The number you entered is invalid. Please try again!");
				modBookInfo();
			}

			break;

		}
	}

	//Modified category name
	public String sortName(int flag) {
		if (flag == 1)
			return "Book Title";
		if (flag == 2)
			return "Author";
		if (flag == 3)
			return "Publication Date";
		if (flag == 4)
			return "Total Pages";
		else
			return "borrower";
	}

	public void delete() {
		System.out.print("Please enter the title of the book to be deleted:");
		Scanner scanner = new Scanner(System.in);
		String bookname = scanner.nextLine();
		int flag = bo.testReader(bookname);
		if (flag == 0) {
			bo.deleteBook(bookname);
			System.out.println(bookname + "Successfully removed from the library! Enter 0 to return to the main menu, enter 1 to continue deleting");
			delReturn();
		} else if (flag == 1) {//Someone borrowed
			System.out.println("This book has been lent, please wait for it to be returned before deleting. Enter 0 to return to the main menu, enter 1 to continue to delete");
			delReturn();
		} else {//This book does not exist
			System.out.println("This book does not exist in this library, please compare it carefully. Enter 0 to return to the main menu, enter 1 to continue to delete");
			delReturn();
		}

	}

	public void delReturn() {

		Scanner scanner1 = new Scanner(System.in);
		int i = scanner1.nextInt();
		if (i == 0) {
			showMenu();
		} else if (i == 1) {
			delete();
		}
	}
	
	public void returnBook() {
		System.out.print("Please enter the name of the book to be returned:");
		Scanner scanner = new Scanner(System.in);
		String bookname = scanner.nextLine();
		int flag = bo.testReader(bookname);
		if (flag == 0) {
			System.out.println(bookname + "Not yet loaned out.");
			System.out.print("Enter 0 and press Enter to return to the main menu, enter other keys to continue returning the book");
			String a = scanner.nextLine();
			if(a.equals("0")){
				showMenu();
			}else {
				returnBook();
			}
		} else if (flag == -1) {
			System.out.println(bookname + "Does not exist in this library.");
			System.out.print("Enter 0 and press Enter to return to the main menu, enter other keys to continue returning the book");
			String a = scanner.nextLine();
			if(a.equals("0")){
				showMenu();
			}else {
				returnBook();
			}
		} else if (flag == 1) {

			System.out.println(bo.getBorrowReader(bookname) + "The book has been returned successfully!");
			bo.setBorrowReader(bookname);
			System.out.print("Enter 0 and press Enter to return to the main menu, enter other keys to continue returning the book");
			String a = scanner.nextLine();
			if(a.equals("0")){
				showMenu();
			}else {
				returnBook();
			}
		}
	}
}