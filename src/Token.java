public class Token {
    private char value;
    private int numberLFSR;

    public Token() {
    }

    public Token(char value, int numberLFSR) {
        this.value = value;
        this.numberLFSR = numberLFSR;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public int getNumberLFSR() {
        return numberLFSR;
    }

    public void setNumberLFSR(int numberLFSR) {
        this.numberLFSR = numberLFSR;
    }

    @Override
    public String toString() {
        return
                value +
                        "-" + numberLFSR +" ";
    }
}
