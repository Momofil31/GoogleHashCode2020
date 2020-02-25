// CREDITS: Marco Lechtaler

package it.unitn.studenti.momesso.filippo;

public class Book implements Comparable<Book> {
    private int index;
    private int score;

    private boolean scanned;

    public Book(int index, int score) {
        this.index = index;
        this.score = score;
        this.scanned = false;
    }


    public int getIndex() {
        return index;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    @Override
    public int compareTo(Book book) {
        if (this.score == book.getScore()) {
            return 0;
        }

        if (this.score > book.score) {
            return -1;
        }

        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (getIndex() != book.getIndex()) return false;
        return getScore() == book.getScore();
    }

    @Override
    public int hashCode() {
        int result = getIndex();
        result = 31 * result + getScore();
        return result;
    }
}
