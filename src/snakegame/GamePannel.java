package snakegame;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.lang.Math.random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GamePannel extends javax.swing.JPanel {

    rawBox board[][] = new rawBox[31][31];//board for values
    int win=0,levelwin=0,score=0,totalmoves=0,level,maxlevel=3;
    
    Border darkborder = new LineBorder(Color.DARK_GRAY, 1);
    Border lightborder = new LineBorder(Color.lightGray, 1);
    Border nullborder = new LineBorder(Color.darkGray, 0);
    
    JPanel stage = new JPanel();
    
    JButton[][] box = new JButton[31][31];
    CardLayout card=new CardLayout();
    
    //game variables
    int snakeDir=1,foodDir=1;
    rawBox headPtr,foodPtr;
    Timer snakeTimer,foodTimer;
    boolean lock=false;
    
    class rawBox{
        int type;
        int x,y;
        rawBox next;
        rawBox prev;
        
        rawBox() {
            this.next = null;
            this.prev = null;
        }
        
    }
    
    public GamePannel() {
        initComponents();
        myinit();
    }
    
    public final void myinit(){
        setSize(820, 620);
        setOpaque(false);
        controlPannel.setBounds(60,80,510,510);
        controlPannel.setLayout(card);
        controlPannel.setFocusable(true);
        controlPannel.setOpaque(false);
        controlPannel.setFocusTraversalKeysEnabled(false);
        controlPannel.addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e){
                keyInput(e);
            }
        });
        
        createBox();
        
        initBoard1();
        
    }
    
    public boolean gameFinish(){
        snakeTimer.stop();
        if(level>1)
            foodTimer.stop();
        levelwin=1;
        if(levelwin==1){
            if(level<maxlevel){
                JOptionPane.showMessageDialog(controlPannel,"move to next level","Level Complete",JOptionPane.INFORMATION_MESSAGE);
                switch(level){
                    case 1:
                        initBoard2();
                        break;
                    case 2:
                        initBoard3();
                        break;
                }
            }
            else if(level==maxlevel){
                win=1;
                JOptionPane.showMessageDialog(controlPannel,"Your Score : "+score+" ","Game Complete",JOptionPane.INFORMATION_MESSAGE);
            }   
        }
        return true;
    }
    
    public void createBox(){
        stage.setLayout(new GridLayout(30,30,0,0));
        for(int i=1;i<31;i++){
            for(int j=1;j<31;j++){
                box[i][j]=new JButton();
                box[i][j].setSize(20,20);
                box[i][j].setBorder(lightborder);
                stage.add(box[i][j]);
            }
        }
        stage.setOpaque(false);
        controlPannel.add(stage);
    }
    
    public void updateGui(){
        for(int i=1;i<31;i++)
            for(int j=1;j<31;j++){
                if(board[i][j].type==0){//null area
                    box[i][j].setIcon(null);
                    box[i][j].setBorder(nullborder);
                    box[i][j].setFocusPainted( false );
                    box[i][j].setContentAreaFilled(false );
               //     box[i][j].setOpaque(false);
                }
                if(board[i][j].type==1){//bricks walls
                    box[i][j].setBorder(nullborder);
                    box[i][j].setBackground(Color.black);
                    box[i][j].setFocusPainted( true );
                    box[i][j].setContentAreaFilled(true );
                }
                if(board[i][j].type==2){//game ground area
                    box[i][j].setBorder(nullborder);
                    box[i][j].setIcon(null);
                    box[i][j].setBackground(Color.white);
                    box[i][j].setFocusPainted( true );
                    box[i][j].setContentAreaFilled(true );
                }
                if(board[i][j].type==3){ //player head
                    box[i][j].setBackground(Color.red);
                    box[i][j].setIcon(null);
                    box[i][j].setBorder(lightborder);
                    box[i][j].setFocusPainted( true );
                    box[i][j].setContentAreaFilled(true );
                }
                if(board[i][j].type==4){ //food
                    box[i][j].setBackground(Color.white);
                    box[i][j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/apple.png")));
                    box[i][j].setBorder(nullborder);
                    box[i][j].setFocusPainted( true );
                    box[i][j].setContentAreaFilled(true );//to make buttons visible
                }
            }
    }
    
    public void initBoard1(){
        levelwin=0;
        level = 1;
        score=0;
        scoreLabel.setText("Score : "+Integer.toString(score));
        
        for(int i=0;i<31;i++)
            for(int j=0;j<31;j++){
                board[i][j]=new rawBox();
                board[i][j].type=2;
                board[i][j].x=j;
                board[i][j].y=i;
            }
        
        for(int i=0;i<31;i++)
            for(int j=0;j<31;j++){
                board[1][j].type=1;
                board[30][j].type=1;
                board[i][30].type=1;
                board[i][1].type=1;    
            }
        
        board[10][5].type=3;//head
        board[10][5].prev=board[10][6];
        board[10][5].next=board[10][4];
        headPtr=board[10][5];
        
        board[10][4].type=3;//body
        board[10][4].prev=board[10][5];
        board[10][4].next=board[10][3];
        
        board[10][3].type=3;//tail
        board[10][3].prev=board[10][4];
        board[10][3].next=null;//means it is tail
        
        board[15][10].type=4;//food
        foodPtr=board[15][10];
        snakeDir = 1;
        
        snakeTimer = new Timer(120,new moveSnake());
        snakeTimer.start();
        
        updateGui();
    } 
    
    public void initBoard2(){
        levelwin=0;
        level = 2;
        scoreLabel.setText("Score : "+Integer.toString(score));
        
        for(int i=0;i<31;i++)//initilize all 2
            for(int j=0;j<31;j++){
                board[i][j]=new rawBox();
                board[i][j].type=2;
                board[i][j].x=j;
                board[i][j].y=i;
            }
        
        for(int i=0;i<31;i++)//initilize all 1
            for(int j=0;j<31;j++){
                board[1][j].type=1;
                board[30][j].type=1;
                board[i][30].type=1;
                board[i][1].type=1;    
            }
        
        board[10][5].type=3;//head
        board[10][5].prev=null;//actually it is useless to define prev to head
        board[10][5].next=board[10][4];
        headPtr=board[10][5];
        
        board[10][4].type=3;//body
        board[10][4].prev=board[10][5];
        board[10][4].next=board[10][3];
        
        board[10][3].type=3;//tail
        board[10][3].prev=board[10][4];
        board[10][3].next=null;//means it is tail
        
        board[15][10].type=4;//food
        foodPtr=board[15][10];
        snakeDir = 1;
        
        snakeTimer = new Timer(120,new moveSnake());
        snakeTimer.start();
        foodTimer = new Timer(820,new moveFood());
        foodTimer.start();
        
        updateGui();//calls update box
    } //initializes board2
    
    public void initBoard3(){
        levelwin=0;
        level = 3;
        scoreLabel.setText("Score : "+Integer.toString(score));
        
        for(int i=0;i<31;i++)//initilize all 2
            for(int j=0;j<31;j++){
                board[i][j]=new rawBox();
                board[i][j].type=2;
                board[i][j].x=j;
                board[i][j].y=i;
            }
        
        for(int i=0;i<31;i++)//initilize all 1
            for(int j=0;j<31;j++){
                board[1][j].type=1;
                board[30][j].type=1;
                board[i][30].type=1;
                board[i][1].type=1;    
            }
        
        board[7][7].type=1;board[7][8].type=1;board[8][7].type=1;board[7][9].type=1;board[9][7].type=1;
        board[7][23].type=1;board[7][22].type=1;board[8][23].type=1;board[7][21].type=1;board[9][23].type=1;
        board[23][7].type=1;board[23][8].type=1;board[22][7].type=1;board[23][9].type=1;board[21][7].type=1;
        board[23][23].type=1;board[22][23].type=1;board[23][22].type=1;board[21][23].type=1;board[23][21].type=1;
         
        
        board[10][5].type=3;//head
        board[10][5].prev=board[10][6];
        board[10][5].next=board[10][4];
        headPtr=board[10][5];
        
        board[10][4].type=3;//body
        board[10][4].prev=board[10][5];
        board[10][4].next=board[10][3];
        
        board[10][3].type=3;//tail
        board[10][3].prev=board[10][4];
        board[10][3].next=null;//means it is tail
        
        board[15][12].type=4;//food
        foodPtr=board[15][12];
        snakeDir = 1;
        
        snakeTimer = new Timer(120,new moveSnake());
        snakeTimer.start();
        foodTimer = new Timer(820,new moveFood());
        foodTimer.start();
        
        updateGui();
    }
        
    public class moveSnake implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            rawBox process = headPtr;
            boolean levelFinish=false;
            int x=headPtr.x,y=headPtr.y,localflag=0;
            switch(snakeDir){
                case 1:
                    if(board[y][x+1].type==2||board[y][x+1].type==4){
                        process.prev=board[y][x+1];
                    }
                    else{
                        levelFinish=true;
                        gameFinish();
                    }
                    break;
                case 2:
                    if(board[y+1][x].type==2||board[y+1][x].type==4){
                        process.prev=board[y+1][x]; 
                    }
                    else{
                        levelFinish=true;
                        gameFinish();
                    }
                    break;
                case 3:
                    if(board[y][x-1].type==2||board[y][x-1].type==4){
                        process.prev=board[y][x-1];  
                    }
                    else{
                        levelFinish=true;
                        gameFinish();
                    }
                    break;
                case 4:
                    if(board[y-1][x].type==2||board[y-1][x].type==4){
                        process.prev=board[y-1][x];                       
                    }
                    else{
                        levelFinish=true;
                        gameFinish();
                    }
                    break;
            }
            
            if(levelFinish==false){//if not finished
                if(process.prev.type==4) localflag=1;

                process.prev.type=3;
                process.prev.prev=null;
                process.prev.next=process;

                headPtr=process.prev;

                process=process.next;

                if(localflag==0){
                    while(process.next!=null&&localflag==0){
                        process=process.next;
                    }//end while

                    process.prev.next=null;
                    process.type=2;    
                }
                else{//localflag==1 i.e got food
                    scoreUpdate();
                    generateFood();
                }
                
                lock=false;
                updateGui();
            }
        }
    }
    
    public class moveFood implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            boolean localFlag=true;
            while(localFlag){
                int localProb = (int)(random()*10);
                int localX=foodPtr.x;int localY=foodPtr.y;

                if(localProb<5 && localProb>0){
                   switch(localProb){
                       case 1:
                            if(board[localY][localX+1].type==2){
                                foodDir=1;
                                foodPtr.type=2;
                                foodPtr=board[localY][localX+1]; 
                                localFlag=false;
                            }
                            break;
                       case 2:
                            if(board[localY+1][localX].type==2){
                                foodDir=2;
                                foodPtr.type=2;
                                foodPtr=board[localY+1][localX];
                                localFlag=false;
                            }
                            break;
                       case 3:
                            if(board[localY][localX-1].type==2){
                                foodDir=3;
                                foodPtr.type=2;
                                foodPtr=board[localY][localX-1];
                                localFlag=false;
                            }
                            break;
                       case 4:
                            if(board[localY-1][localX].type==2){
                                foodDir=4;
                                foodPtr.type=2;
                                foodPtr=board[localY-1][localX];
                                localFlag=false;
                            }
                            break;
                   }//end switch localProb                   
                }//end if
                else { // move on same direction
                    switch(foodDir){
                       case 1:
                            if(board[localY][localX+1].type==2){
                                foodDir=1;
                                foodPtr.type=2;
                                foodPtr=board[localY][localX+1]; 
                                localFlag=false;
                            }
                            break;
                       case 2:
                            if(board[localY+1][localX].type==2){
                                foodDir=2;
                                foodPtr.type=2;
                                foodPtr=board[localY+1][localX];
                                localFlag=false;
                            }
                            break;
                       case 3:
                            if(board[localY][localX-1].type==2){
                                foodDir=3;
                                foodPtr.type=2;
                                foodPtr=board[localY][localX-1];
                                localFlag=false;
                            }
                            break;
                       case 4:
                            if(board[localY-1][localX].type==2){
                                foodDir=4;
                                foodPtr.type=2;
                                foodPtr=board[localY-1][localX];
                                localFlag=false;
                            }
                            break;
                   }//end switch foodDir
                }
            }//end while
            foodPtr.type=4;
            updateGui();
        }
    }
    
    public void generateFood(){
        int x,y;
        while(true){
            x=(int)(random()*30)+1;
            y=(int)(random()*30)+1;

            if(board[y][x].type==2){
                foodPtr=board[y][x];
                foodPtr.type=4;
                break;
            }
        }
    }
   
    public void scoreUpdate(){
        score+=level;
        scoreLabel.setText("Score : "+Integer.toString(score));
    }
    
    public void keyInput(KeyEvent e){
       char input=e.getKeyChar();
        if(lock==false){
            if(input=='w' || input=='W'){
                if(snakeDir==1 || snakeDir==3){
                    snakeDir=4;
                    lock=true;
                }
            }
            if(input=='s' || input=='S'){
                if(snakeDir==1 || snakeDir==3){
                    snakeDir=2;
                    lock=true;
                }
            }
            if(input=='a' || input=='A'){
                if(snakeDir==4 || snakeDir==2){
                    snakeDir=3;
                    lock=true;
                }
            }
            if(input=='d' || input=='D'){
                if(snakeDir==4 || snakeDir==2){
                    snakeDir=1;
                    lock=true;
                }
            }
        }
        if(input=='l' || input=='L'){
            new moveSnake().actionPerformed(new ActionEvent(score, win, TOOL_TIP_TEXT_KEY));//debugg mover
        }
        if(input==' '){
            resumeButtonActionPerformed(new ActionEvent(score, win, TOOL_TIP_TEXT_KEY));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        heading = new javax.swing.JLabel();
        controlPannel = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        resumeButton = new javax.swing.JButton();
        scoreLabel = new javax.swing.JLabel();
        background_Image = new javax.swing.JLabel();

        setLayout(null);

        heading.setFont(new java.awt.Font("DejaVu Serif", 1, 48)); // NOI18N
        heading.setForeground(new java.awt.Color(255, 102, 102));
        heading.setText("Snake");
        add(heading);
        heading.setBounds(220, -10, 250, 90);

        javax.swing.GroupLayout controlPannelLayout = new javax.swing.GroupLayout(controlPannel);
        controlPannel.setLayout(controlPannelLayout);
        controlPannelLayout.setHorizontalGroup(
            controlPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        controlPannelLayout.setVerticalGroup(
            controlPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(controlPannel);
        controlPannel.setBounds(40, 85, 585, 495);
        controlPannel.getAccessibleContext().setAccessibleName("");

        newButton.setText("Resetear");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        add(newButton);
        newButton.setBounds(670, 360, 110, 22);

        helpButton.setText("Ayuda");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });
        add(helpButton);
        helpButton.setBounds(670, 440, 110, 22);

        resumeButton.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        resumeButton.setText("Pausa");
        resumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeButtonActionPerformed(evt);
            }
        });
        add(resumeButton);
        resumeButton.setBounds(670, 510, 110, 20);

        scoreLabel.setFont(new java.awt.Font("TakaoPGothic", 1, 24)); // NOI18N
        scoreLabel.setForeground(new java.awt.Color(252, 236, 236));
        scoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        scoreLabel.setText("Score : 0");
        add(scoreLabel);
        scoreLabel.setBounds(630, 210, 180, 50);

        background_Image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gamePannelBackground.jpg"))); // NOI18N
        add(background_Image);
        background_Image.setBounds(0, 0, 820, 620);
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        snakeTimer.stop();
        if(level>1)
            foodTimer.stop();
        switch(level){
            case 1:
                initBoard1();
                break;
            case 2:
                initBoard2();
                break;
            case 3:
                initBoard3();
                break;
        }
        
        controlPannel.grabFocus();//get focus back to control pannel
    }//GEN-LAST:event_newButtonActionPerformed

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        snakeTimer.stop();
        if(level>1)
            foodTimer.stop();
        JOptionPane.showMessageDialog(controlPannel,"* usa W A S D keys para mover snake\n"
                                            +"* presiona espacio para pausar\n"
                                            ,"Instrucciones"
                                            ,JOptionPane.INFORMATION_MESSAGE                                    
        );
        controlPannel.grabFocus();
    }//GEN-LAST:event_helpButtonActionPerformed

    private void resumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumeButtonActionPerformed
        if(snakeTimer.isRunning()==false){
            snakeTimer.start();
            if(level>1)
                foodTimer.start();
        }
        else{
            snakeTimer.stop();
            if(level>1)
                foodTimer.stop();
        }
        controlPannel.grabFocus();
    }//GEN-LAST:event_resumeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background_Image;
    private javax.swing.JPanel controlPannel;
    private javax.swing.JLabel heading;
    private javax.swing.JButton helpButton;
    private javax.swing.JButton newButton;
    private javax.swing.JButton resumeButton;
    private javax.swing.JLabel scoreLabel;
    // End of variables declaration//GEN-END:variables
}
