package Game;

import java.util.*;

//게임의 기본적인 세팅 그리드 사이즈 및 잠수함 위치시키기
public class GameHelper {
    private static class Grid { //실질적인 게임진행을 담당하는 Grid InnerClass 이를 이용해서 ArrayList 구현
        int grid; //좌푯값 정수
        String pos;//좌푯값 문자열
        boolean isUse = false; //해당 좌표를 다른 잠수함 객체가 사용중인지 체크
        SubM sub=new SubM(); //각 잠수함객체
        Grid(int g,String p){ //생성자
            grid=g;
            pos=p;
        }
        Grid next; //연결되어있는 함체의 다음칸을 나타냄
        public void Use(){isUse= !isUse;} //사용 -> 미사용 , 미사용->사용 전환함수
        public void setNext(Grid temp) { next= temp;} //연결된 다음객체 지정함수
        public Grid getNext() {return next;}//연결된 다음객체 받아오는 함수

    }

    private static final String alphabet="abcdefghijklmnopqrstuvwxyz"; //문자열좌푯값 알파벳 저장 문자열
    private static int gridLength; // 행의 길이
    private final int gridSize;  // 게임판 전체 사이즈 gridLength*gridLength
    private ArrayList<Grid> grid =new ArrayList<Grid>(); //ArrayList를 통한 논리적 배열만들기
    GameHelper(){
        this(10);
    } //게임판의 디폴트값은 10*10
    GameHelper(int gridLength) { //생성자를통해 게임판 사이즈 지정
        this.gridLength=gridLength;
        gridSize=gridLength*gridLength;
        SetGrid();

    }





    private void SetGrid(){ //그리드 배치
            int row=0,cnt=0; //문자열 좌푯값을 위한 row ,정수좌푯값을 위한 cnt
            String temp; //붙여줄 문자열좌푯값을 만드는 임시 변수
            for(row=0;row<gridLength;row++) { //반복문을 통하여 각 그리드에 문자열좌표, 정수좌표 오름차순 삽입
                for (int i = 0; i < gridLength; i++) {
                    temp = String.valueOf(alphabet.charAt(row));
                    temp = temp.concat(String.valueOf(i));
                    grid.add(new Grid(cnt++, temp));
                }
            }
    }

    public void PlaceSubMarine(SubM marine) { //잠수함 배치
        int attempt= marine.getSize(),rand; //시도횟수는 총 객체길이 만큼 수행시기 , rand 는 무작위로 선택된 숫자에 삽입
        int select; //상하좌우중 하나를 무작위 선택하여 받는 변수
        rand=(int)((Math.random()*10000)%(gridSize-1)); //게임판 사이즈 내의 무작위 값을 입력받음
            if(!grid.get(rand).isUse) { //사용중이 아니라면
                grid.get(rand).isUse=true; //사용중으로 전환
                grid.get(rand).sub.setName(marine.getName()); //잠수함 해당 잠수함에 이름 넣기
                grid.get(rand).sub.setHeader(); //최초삽입된 객체를 헤더로지정
                while(attempt-- !=0) { //잠수함의 길이 만큼 반복
                    select=(int)((Math.random()*10000)%3); //0상 1하 2좌 3우 의 값을 랜덤으로 선택받음
                    while(true) { //상하좌우로 선택할 위치를 지정
                        if(select ==0 && (rand <gridLength || grid.get(rand-gridLength).isUse) ) {
                            select=(int)((Math.random()*10000)%4);
                        } //위쪽으로 배치 할경우 예외처리 더이상위가 없거나 이미 다른 객체가 사용중일때 다시 랜덤값지정
                        else if(select ==1 && (rand >gridSize-gridLength || grid.get(rand+gridLength).isUse) ) {
                            select=(int)((Math.random()*10000)%4);
                        }//동일
                        else if(select ==2 && (rand%gridLength ==0 || grid.get(rand-1).isUse) ) {
                            select=(int)((Math.random()*10000)%4);
                        }//동일
                        else if(select ==3 && (rand%(gridLength-1) ==gridSize || grid.get(rand+1).isUse) ) {
                            select=(int)((Math.random()*10000)%4);
                        }//동일
                        else { //위쪽 예외에 걸리지 않을경우 배치 수행
                            switch (select) {
                                case 0 -> {
                                    grid.get(rand).setNext(grid.get(rand-gridLength)); //다음 객체의 연결을 통해 뱀처럼 각 객체가 논리적으로 연결지어짐
                                    rand -= gridLength; // 다음 객체 주변으로 배치해야하므로 rand값을 수정
                                    grid.get(rand).isUse = true; //사용중으로 전환
                                    grid.get(rand).sub.setName(marine.getName());// 객체의 이름을지정
                                }
                                case 1 -> { //위와 동일
                                    grid.get(rand).setNext(grid.get(rand+gridLength));
                                    rand += gridLength;
                                    grid.get(rand).isUse = true;
                                    grid.get(rand).sub.setName(marine.getName());
                                }
                                case 2 -> { //위와동일
                                    grid.get(rand).setNext(grid.get(rand-1));
                                    rand -= 1;
                                    grid.get(rand).isUse = true;
                                    grid.get(rand).sub.setName(marine.getName());
                                }
                                case 3 -> {//위와동일
                                    grid.get(rand).setNext(grid.get(rand+1));
                                    rand += 1;
                                    grid.get(rand).isUse = true;
                                    grid.get(rand).sub.setName(marine.getName());
                                }
                            }
                            break;
                        }
                    }
                }


            }
            else{ //제일 처음 배치 실패 했을경우 재귀호출
                PlaceSubMarine(marine);
        }
    }
    public void MoveSubMarine(SubM marine){ //잠수함 움직이기 매개변수로 움직일 잠수함객체를 받아옴
        Scanner sc=new Scanner(System.in); //이동 방향은 직접입력이므로 Scanner 이용
        char[] move =new char[marine.getMove()+1]; //이동거리는 잠수함에서 지정된 이동거리를 넘지않음 기본:4
        System.out.println("이동키 입력 [w: 위, s: 아래, a: 좌, d: 우]");
        move=sc.next().toCharArray();//char[] 형으로 받아 하나하나 읽어서 작동하도록함
        Grid Head=new Grid(0,null); //객체의 헤더부분을 찾아 저장시킬 임시변수
        for (int i=0;i<gridSize;i++) { //객체 헤더찾기
            if(grid.get(i).sub.getName()== marine.getName() && grid.get(i).sub.getHeader()) { //처음부터 순회해서 헤더부분을 찾음
                Head=grid.get(i);
                break; //찾을경우 탈출
            }
        }
        Grid temp=new Grid(0,null); //꼬리부분 임시변수
        int up,index; //이동시킬위치변수, 꼬리부분 변수
        for(int i=0;i< move.length;i++) { //문자열의 길이만큼 반복
            temp=Head; //temp와 head를 이어준후 getNext()함수를 이용하여 꼬리부분찾기
            while(true) {
                if(temp.getNext()!=null) //꼬리부분 찾기
                {
                    temp= temp.getNext();

                }

                else
                    break;
            }//while문을 벗어난후 temp는 꼬리부분을 갖게됨 객체를 배치하는과정에서 next가 null인부분이 마지막이기 때문
            switch (move[i]) {
                case 'w': //상
                    if(Head.grid-gridLength <0 || grid.get(Head.grid-gridLength).isUse) {
                        System.out.println("상 이동 실패");
                    }//위로 이동할공간이 더이상없을경우 실패
                    else {
                        up= Head.grid-gridLength; //헤더부분 좌표
                        index=temp.grid; //꼬리부분 좌표
                        Grid tSub= new Grid(grid.get(index).grid,grid.get(index).pos);// 꼬리부분을 바꿔주기위한 grid
                        tSub.isUse=false; //사용중이지 않은상태로전환
                        grid.get(up).sub.setCell(grid.get(index).sub.getCell()); //현재 가지고있던 체력을 옮기는 과정
                        grid.set(index,tSub);//꼬리객체 지우기
                        grid.get(index).setNext(null);//꼬리의 다음부분 null로 지정
                        grid.get(index).sub.setName(null);//꼬리부분의 이름을 null로 지정
                        grid.get(up).Use(); //이동부분 상태 설정
                        grid.get(Head.grid).sub.reHeader(); //기존 헤드 상태 제거
                        grid.get(up).sub.setName(Head.sub.getName()); //이동부분 이름지정
                        grid.get(up).sub.setHeader(); // 이동부분 헤더로 지정
                        grid.get(up).setNext(Head); //이동된 헤더의 다음객체연결을 이전 헤더로지정
                        while(true) { //새로운 꼬리를 정해주는 과정
                            if(Head.getNext().getNext()==null) {
                                Head.setNext(null);
                                break;
                            }
                            else
                                Head=Head.getNext();
                        }
                        Head=grid.get(up); //헤더 변수 를 새로운헤더로 지정
                    }
                    break;
                case 's': //하
                    if(Head.grid+gridLength >gridSize || grid.get(Head.grid+gridLength).isUse) {
                        System.out.println("하 이동 실패");
                    }
                    else {


                        up= Head.grid+gridLength;
                        index=temp.grid; //꼬리부분 좌표
                        Grid tSub= new Grid(grid.get(index).grid,grid.get(index).pos);
                        tSub.isUse=false;
                        grid.get(up).sub.setCell(grid.get(index).sub.getCell());
                        grid.set(index,tSub);
                        grid.get(index).setNext(null);
                        grid.get(index).sub.setName(null);
                        grid.get(up).Use(); //이동부분 상태 설정
                        grid.get(Head.grid).sub.reHeader(); //기존 헤드 상태 제거
                        grid.get(up).sub.setName(Head.sub.getName()); //이동부분 이름지정
                        grid.get(up).sub.setHeader(); // 이동부분 헤더로 지정
                        grid.get(up).setNext(Head); //이동된 헤더의 다음객체연결을 이전 헤더로지정
                        while(true) {
                            if(Head.getNext().getNext()==null) {
                                Head.setNext(null);
                                break;
                            }
                            else
                                Head=Head.getNext();
                        }
                        Head=grid.get(up); //헤더 변수 를 새로운헤더로 지정
                    }
                    break;

                case 'd': //우
                    if((Head.grid+1)%(gridLength)==0|| grid.get(Head.grid+1).isUse) {
                        System.out.println("우 이동 실패");
                    }
                    else {

                        up= Head.grid+1;
                        index=temp.grid; //꼬리부분 좌표
                        Grid tSub= new Grid(grid.get(index).grid,grid.get(index).pos);
                        tSub.isUse=false;
                        grid.get(up).sub.setCell(grid.get(index).sub.getCell());
                        grid.set(index,tSub);
                        grid.get(index).setNext(null);
                        grid.get(index).sub.setName(null);
                        grid.get(up).Use(); //이동부분 상태 설정
                        grid.get(Head.grid).sub.reHeader(); //기존 헤드 상태 제거
                        grid.get(up).sub.setName(Head.sub.getName()); //이동부분 이름지정
                        grid.get(up).sub.setHeader(); // 이동부분 헤더로 지정
                        grid.get(up).setNext(Head); //이동된 헤더의 다음객체연결을 이전 헤더로지정
                        while(true) {
                            if(Head.getNext().getNext()==null) {
                                Head.setNext(null);
                                break;
                            }
                            else
                                Head=Head.getNext();
                        }
                        Head=grid.get(up); //헤더 변수 를 새로운헤더로 지정
                    }
                    break;
                case 'a': //좌
                    if(((Head.grid-1)%(gridLength))==gridLength-1|| grid.get(Head.grid-1).isUse) {
                        System.out.println("좌 이동 실패");
                    }
                    else {

                        up= Head.grid-1;
                        index=temp.grid; //꼬리부분 좌표
                        Grid tSub= new Grid(grid.get(index).grid,grid.get(index).pos);
                        tSub.isUse=false;
                        grid.get(up).sub.setCell(grid.get(index).sub.getCell());
                        grid.set(index,tSub);
                        grid.get(index).setNext(null);
                        grid.get(index).sub.setName(null);
                        grid.get(up).Use(); //이동부분 상태 설정
                        grid.get(Head.grid).sub.reHeader(); //기존 헤드 상태 제거
                        grid.get(up).sub.setName(Head.sub.getName()); //이동부분 이름지정
                        grid.get(up).sub.setHeader(); // 이동부분 헤더로 지정
                        grid.get(up).setNext(Head); //이동된 헤더의 다음객체연결을 이전 헤더로지정
                        while(true) {
                            if(Head.getNext().getNext()==null) {
                                Head.setNext(null);
                                break;
                            }
                            else
                                Head=Head.getNext();
                        }
                        Head=grid.get(up); //헤더 변수 를 새로운헤더로 지정
                    }
                    break;
                default:

            }
        }
    }
    public void GridAttack(SubM marine) {//잠수함 공격 지정된 좌표의 십자가모양으로 공격실시
        String atkPos; //잠수함 공격위치 저장변수
        Scanner sc= new Scanner(System.in); //직접입력이므로 Scanner사용
        System.out.println("공격할 좌표를 입력하세요. ex) e7 ");
        int row=0; //행
        int col=0;//열
        atkPos=sc.next(); //공격좌표 문자열로 입력받음
        String temp;// 공격 순회할 임시변수
        while(alphabet.charAt(row)!=atkPos.charAt(0)) row++; //공격할 행고정 지정
        col=Integer.parseInt(atkPos.substring(1)); //공격할 열고정 지정
        for(int i=0;i<gridLength;i++) { //행 공격
            temp=atkPos.charAt(0)+String.valueOf(i);
            if(grid.get((row*gridLength)+i).isUse) {
                grid.get((row*gridLength)+i).sub.setCell(grid.get(row*gridLength+i).sub.getCell()-1);
                System.out.printf("%s에 객체 발견 및 공격 수행  남은 체력: %d\n",temp,grid.get((row*gridLength)+i).sub.getCell());
            }
        }
        for(int i=0;i<gridLength;i++) { //열 공격
            if(grid.get((i*gridLength) + col).isUse) {
                grid.get((i*gridLength)+col).sub.setCell(grid.get((i*gridLength)+col).sub.getCell()-1);
                System.out.printf("%s에 객체 발견 및 공격 수행  남은 체력: %d\n",grid.get(i*gridLength +col).pos,grid.get((i*gridLength)+col).sub.getCell());
            }
        }
    }
    public boolean IsEnd() { //cell이 0이하로 내려갈경우 끝났음을 알려주는 함수
        for(Grid i:  grid) { //반복문을통하여 모든 객체를 참조하여 체력이 떨어진 객체를 찾음
            if(i.sub.getCell()<=0) {
                System.out.printf("%s 함선의 %s좌표의 함체의 체력이 0이하로 떨어져 파괴되었습니다!\n\n",i.sub.getName(),i.pos);
                return true; //하나라도 찾을경우 true
            }
        }
        return false; //못찾을경우 false 반환
    }
    public void printGrid(SubM marine){ //그리드 출력
        int cnt=0;
        for(Grid i : grid) {
            cnt++;
            if(i.isUse && !i.sub.getHeader() && marine.getName()== i.sub.getName()) { //출력할 객체 이름과 일치하고 헤더부분이 아닐경우
                System.out.printf("%3c ", '●');
            }
            else if(i.sub.getHeader()&&marine.getName()== i.sub.getName()){ //출력할 객체이름 이 일치하고 헤더부분일경우
                System.out.printf("%3c ",'■');
            }
            else {
                System.out.printf("%3s ",i.pos); //그외의경우에는 문자열 좌푯값을 출력함
            }
            if(cnt%gridLength==0) //줄바꿈
                System.out.println();

        }

    }
}
