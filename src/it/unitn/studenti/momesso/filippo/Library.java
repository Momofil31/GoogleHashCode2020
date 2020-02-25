// CREDITS: Marco Lechtaler

package it.unitn.studenti.momesso.filippo;

import java.util.ArrayList;
import java.util.List;

public class Library implements Comparable<Library> {
    private int index;
    private int signupTime;
    private int booksPerDay;
    private int bookInLibrary;
    private List<Book> books;

    private int totalBookScore;

    private int specificScore;


    public Library(int index, int signupTime, int booksPerDay, int bookInLibrary) {
        this.index = index;
        this.signupTime = signupTime;
        this.booksPerDay = booksPerDay;
        this.bookInLibrary = bookInLibrary;
        this.books = new ArrayList<>(bookInLibrary);

        this.totalBookScore = 0;
    }

    public int getSignupTime() {
        return signupTime;
    }

    public void setSignupTime(int signupTime) {
        this.signupTime = signupTime;
    }

    public int getBooksPerDay() {
        return booksPerDay;
    }

    public void setBooksPerDay(int booksPerDay) {
        this.booksPerDay = booksPerDay;
    }

    public void addBook(Book b) {
        this.books.add(b);
    }

    public int getBookInLibrary() {
        return bookInLibrary;
    }

    public int getSpecificScore() {
        return specificScore;
    }

    public int getIndex() {
        return index;
    }

    public List<Book> getBooks() {
        return books;
    }

    public int score() {
        int score = 0;
        for (Book b : this.books) {
            if (b.isScanned()) {
                books.remove(b);
            } else {
                score += b.getScore();
            }
        }
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Library library = (Library) o;

        if (getSignupTime() != library.getSignupTime()) return false;
        return getBooksPerDay() == library.getBooksPerDay();
    }

    @Override
    public int hashCode() {
        int result = getSignupTime();
        result = 31 * result + getBooksPerDay();
        return result;
    }

    @Override
    public int compareTo(Library library) {
        return sortBySpecificScore(library);
    }

    private int sortBySpecificScore(Library library) {
        //decrescente
        if (this.specificScore == library.getSpecificScore()) {
            return 0;
        }

        if (this.specificScore > library.getSpecificScore()) {
            return -1;
        } else {
            return 1;
        }


    }

    private int calcualteSpecificScore() {
//        return totalBookScore;

        //TODO cambia fattore per dare più o meno peso al signupTime
        return totalBookScore / (signupTime * 1);

//        return (totalBookScore * booksPerDay) / signupTime;
//        return (totalBookScore * booksPerDay) / (signupTime*2);
//        return (totalBookScore * booksPerDay) / (signupTime * signupTime);
    }

    public void updateTotalBookScore(int dayRemaining) {
        dayRemaining -= this.signupTime;

        if (dayRemaining < 0)
            dayRemaining = 0;

        int bookMaxThroughput = dayRemaining * this.booksPerDay;

        this.totalBookScore = 0;
        List<Book> tmp = new ArrayList<>();

        int countBook = 0;
        //per fortuna sono già ordinati o comunque li considero tali
        for (Book b : books) {
            if (!b.isScanned()) {
                tmp.add(b);
                countBook++;
                //quando supero il throughput è inutile contare lo score, ma li aggiungo lo stesso alla nuova lista
                if (countBook <= bookMaxThroughput) {
                    this.totalBookScore += b.getScore();
                }
            }
        }
        this.books = tmp;
    }

    public void calcData() {
        this.specificScore = calcualteSpecificScore();
    }

}
