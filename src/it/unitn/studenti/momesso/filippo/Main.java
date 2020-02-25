// CREDITS: Marco Lechtaler

package it.unitn.studenti.momesso.filippo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static int nBooks;
    private static int nLibraries;
    private static int nDays;

    public static void main(String[] args) throws IOException {

        int totalScore = 0;

        for (int f = 0; f < 6; f++) {
//        for (int f = 0; f < 1; f++) {
//        for (int f = 1; f < 2; f++) {

            System.out.println("\nPROCESSING file " + f);
            //ordina librerie per signup
            Scanner fileScanner = new Scanner(new File(f + ".txt"));

            nBooks = fileScanner.nextInt();
            nLibraries = fileScanner.nextInt();
            nDays = fileScanner.nextInt();

            ArrayList<Book> books = new ArrayList<>(nBooks);
            for (int i = 0; i < nBooks; i++) {
                books.add(new Book(i, fileScanner.nextInt()));
            }

            ArrayList<Library> libraries = new ArrayList<>(nLibraries);
            for (int i = 0; i < nLibraries; i++) {
                int nBooksInLibraries = fileScanner.nextInt();
                int signUpProcess = fileScanner.nextInt();
                int maxShips = fileScanner.nextInt();

                Library tmp = new Library(i, signUpProcess, maxShips, nBooksInLibraries);
                for (int j = 0; j < nBooksInLibraries; j++) {
                    tmp.addBook(books.get(fileScanner.nextInt()));
                }
                //TODO check if this sort work
                Collections.sort(tmp.getBooks());

                libraries.add(tmp);
            }


            int score = sol4(libraries,books,"out" + f + ".txt");
            totalScore += score;
            System.out.println("END " + f + " with score: \t" + score);


        }
        System.out.println("\n\n\t\tTOTAL SCORE: " + totalScore);
    }

    private static void printFile(String toPrint, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(toPrint);
        writer.close();
    }

    private static int sol4(ArrayList<Library> libraries,ArrayList<Book> books, String filename) throws IOException {
        //DEBUG
        int score = 0;

        //OUTPUT DATA
        ArrayList<String> datiLibrerie = new ArrayList<>();
        ArrayList<String> datiLibri = new ArrayList<>();


        //INIT SOLUTION
        //First time calc parameters
        for (Library l : libraries) {
            l.updateTotalBookScore(nDays);
            l.calcData();
        }

        //SORT first time
        Collections.sort(libraries);

        //ALGO
        int countLibraries = 0;
        int nDayRemainSignup = nDays;

        while (nDayRemainSignup > 0 && libraries.size() > 0) {
            Library l = libraries.get(0);

            //>= 1 perché è inutile che faccio signup senza sparare fuori niente
            if (nDayRemainSignup - l.getSignupTime() >= 1) {
                countLibraries++;
                nDayRemainSignup -= l.getSignupTime();
                double nDayPerLib = nDayRemainSignup;

                //nDayRemainSignup giorni di throuput
                int nBook = 0;
                StringBuilder tmp = new StringBuilder();

                //sort perché voglio prima i più redditizi
                //TODO non dovrebbe servire perché li sorto la prima volta poi li rimuovo
                // Collections.sort(l.getBooks());

                for (Book b : l.getBooks()) {
                    if (!b.isScanned()) {
                        // non supero i libri al giorno
                        double offset = 1.0 / l.getBooksPerDay();
                        if (nDayPerLib - offset >= 0) {
                            nDayPerLib -= offset;
                            b.setScanned(true);
                            nBook++;
                            tmp.append(" ").append(b.getIndex());

                            //DEBUG
                            score += books.get(b.getIndex()).getScore();
                        }
                    }
                }

                if (nBook > 0) {
                    datiLibrerie.add(l.getIndex() + " " + nBook);
                    datiLibri.add(tmp.toString().trim());
                } else {
                    countLibraries--;
                }
            }

            //REMOVE this library
            libraries.remove(0);
            //Cycle and update all score and stuff to sort library with the new score
            for (Library tmp : libraries) {
                tmp.updateTotalBookScore(nDayRemainSignup);
                tmp.calcData();
            }
            Collections.sort(libraries);

        }

        StringBuilder toRtn = new StringBuilder(countLibraries + "\n");
        for (int i = 0; i < datiLibrerie.size(); i++) {
            toRtn.append(datiLibrerie.get(i));
            toRtn.append("\n");
            toRtn.append(datiLibri.get(i));
            toRtn.append("\n");
        }


        printFile(toRtn.toString(),filename);

        return score;

    }

    /*
    private static String sol3(ArrayList<Library> libraries) {
        Collections.sort(libraries);

        ArrayList<String> datiLibrerie = new ArrayList<>();
        ArrayList<String> datiLibri = new ArrayList<>();


        int countLibraries = 0;
        int nDayRemainSignup = nDays;


        while (nDayRemainSignup > 0 && libraries.size() > 0) {
            Library l = libraries.get(0);

            if (nDayRemainSignup - l.getSignupTime() >= 0) {
                countLibraries++;
                nDayRemainSignup -= l.getSignupTime();
                double nDayPerLib = nDayRemainSignup;

                //nDayRemainSignup giorni di throuput
                int nBook = 0;
                StringBuilder tmp = new StringBuilder();
                for (Book b : l.getBooks()) {
                    if (!b.isScanned()) {
                        // non supero i libri al giorno
                        double offset = 1.0 / l.getBooksPerDay();
                        if (nDayPerLib - offset >= 0) {
                            nDayPerLib -= offset;
                            b.setScanned(true);
                            nBook++;
                            tmp.append(" ").append(b.getIndex());
                        }
                    }
                }

                if (nBook > 0) {
                    datiLibrerie.add(l.getIndex() + " " + nBook);
                    datiLibri.add(tmp.toString().trim());
                } else {
                    countLibraries--;
                }
            }

            libraries.remove(0);
            for (Library tmp : libraries) {
                tmp.updateTotalBookScore();
                tmp.calcData();
            }
            Collections.sort(libraries);

        }

        StringBuilder toRtn = new StringBuilder(countLibraries + "\n");
        for (int i = 0; i < datiLibrerie.size(); i++) {
            toRtn.append(datiLibrerie.get(i));
            toRtn.append("\n");
            toRtn.append(datiLibri.get(i));
            toRtn.append("\n");
        }


        return toRtn.toString();

    }

    private static String sol2(ArrayList<Library> libraries) {
        Collections.sort(libraries);

        ArrayList<String> datiLibrerie = new ArrayList<>();
        ArrayList<String> datiLibri = new ArrayList<>();


        int countLibraries = 0;
        int nDayRemainSignup = nDays;


        for (Library l : libraries) {
            if (nDayRemainSignup - l.getSignupTime() >= 0) {
                countLibraries++;
                nDayRemainSignup -= l.getSignupTime();
                double nDayPerLib = nDayRemainSignup;

                //nDayRemainSignup giorni di throuput
                int nBook = 0;
                StringBuilder tmp = new StringBuilder();
                for (Book b : l.getBooks()) {
                    if (!b.isScanned()) {

                        // non supero i libri al giorno
                        double offset = 1.0 / l.getBooksPerDay();
                        if (nDayPerLib - offset > 0) {

                            nDayPerLib -= offset;
                            b.setScanned(true);
                            nBook++;
                            tmp.append(" ").append(b.getIndex());
                        }
                    }
                }

                if (nBook > 0) {
                    datiLibrerie.add(l.getIndex() + " " + nBook);
                    datiLibri.add(tmp.toString().trim());
                } else {
                    countLibraries--;
                }
            }
        }

        StringBuilder toRtn = new StringBuilder(countLibraries + "\n");
        for (int i = 0; i < datiLibrerie.size(); i++) {
            toRtn.append(datiLibrerie.get(i));
            toRtn.append("\n");
            toRtn.append(datiLibri.get(i));
            toRtn.append("\n");
        }


        return toRtn.toString();

    }

    private static String sol1(ArrayList<Library> libraries) {
        Collections.sort(libraries);

        ArrayList<String> datiLibrerie = new ArrayList<>();
        ArrayList<String> datiLibri = new ArrayList<>();


        int countLibraries = 0;
        int nDayRemainSignup = nDays;


        for (Library l : libraries) {
            countLibraries++;
            nDayRemainSignup -= l.getSignupTime();

            datiLibrerie.add(l.getIndex() + " " + l.getBookInLibrary());

            StringBuilder tmp = new StringBuilder();
            for (Book b : l.getBooks()) {
                tmp.append(" ").append(b.getIndex());
            }
            datiLibri.add(tmp.toString().trim());
        }

        StringBuilder toRtn = new StringBuilder(countLibraries + "\n");
        for (int i = 0; i < datiLibrerie.size(); i++) {
            toRtn.append(datiLibrerie.get(i));
            toRtn.append("\n");
            toRtn.append(datiLibri.get(i));
            toRtn.append("\n");
        }


        return toRtn.toString();

    }
    */


}
