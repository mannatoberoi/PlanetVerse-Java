package main;

import dao.TransactionalEnrollmentDAO;

public class TestDatabaseTransactions {

    public static void main(String[] args) {

        TransactionalEnrollmentDAO dao =
                new TransactionalEnrollmentDAO();

        System.out.println("Testing duplicate enrollment...");

        int result = dao.enroll(9998, 5001);

        if (result == -1) {
            System.out.println(
                "PASS: Duplicate enrollment was prevented."
            );
        } else {
            System.out.println(
                "Unexpected enrollment ID: " + result
            );
        }
    }
}