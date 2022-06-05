package Game;
//각 잠수함의 부모객체

public class SubM {
    private String name=null; //이름
    private int cell,size,move; //cell 체력 ,size 잠수함 크기, move 이동거리
    private boolean Header=false; //헤더인지 확인

    SubM() {
        size=3;
        move=4;
        cell=5;
    }
    SubM(String name) {
        this();
        this.name= name;
    }
    public int getCell() {return cell;} // getter setter 함수들
    public void setCell(int cell) {this.cell=cell;}
    public int getMove(){return move;}
    public boolean getHeader(){return Header;}
    public void setHeader(){Header=true;}
    public void reHeader(){Header=false;}
    public int getSize(){return size;}
    public void setName (String n) { name = n; }
    public String getName() { return name; }

}
