import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Player controller maniplates the data in the player and handles input to the player.
 * I could change this so that player is really just a nested class to PlayerController
 * and access only allowed to PlayerController to change Player data
 * that will enforce this design.
 */
public class PlayerController extends Observer
{

    boolean takingInput = false;
    float absorbCD = 10.0f;
    float timeTillAbsorb = 0;
    Player player;
    
    //Player attacks
    StraightShot fireAttack;
    
    //Pool for player attacks could be here.
    
    PoolManager playerAttackPools;

    public PlayerController(Player subject){
   
        playerAttackPools = new PoolManager();
        this.observe(subject);
        priority = 1;
        threadName = "pc";
       
        initPlayerAttacks();
    }
    
    private void initPlayerAttacks(){
        
        
        //It might better if just have AttackManager that pools all the attacks
        //and will basically be exact same as ItemManager...Then should I make amnager class? or just put it in ItemManager, that
        
        //Could be something else, whatever.
        fireAttack = new StraightShot();
        //Set image to be fire image.
        
        playerAttackPools.addPool("fireAttack",fireAttack,20);
        
        //Set up other attacks.
        
    }
   
    public void observe(Subject subject){
        
        super.observe(subject);
        player = (Player)subject;
        takingInput = true;
    }

    public void react() 
    {

        //Reaction code to changes in state to subject go here

            if (player.getCurrentState() == State.DAMAGED || 
                player.getCurrentState() == PlayerState.TRANSFORMING || player.getCurrentState() == PlayerState.PAUSED){
                
                //Stop taking input for movement, attacking, while transformation is happening, ie: animation still playing.
                takingInput = false;
              
                //Indefinitely invincible if paused
                if (player.getCurrentState() == PlayerState.PAUSED){
                    player.becomeInvincible(-1);
                }
                //also shouldn't be taking damage anymore as player so become invincible
                
                if (subject.getCurrentState() == PlayerState.TRANSFORMING){
                                        
                    System.out.println("Transforming");
                    //Changes subject to Player
                    //I may need to reset what others are observing
                    Player prevPlayer = player;
                    Player newPlayer = PlayerFactory.produce(prevPlayer.getCurrentTransformation());
                    
                    newPlayer.setHealth(prevPlayer.getHealth());
                   
                    prevPlayer.copyObservers(newPlayer);   
                    
                    getWorld().addObject(newPlayer,prevPlayer.getX(), prevPlayer.getY());                 
                    getWorld().removeObject(prevPlayer);                    
                    
                    System.out.println(player);
                }
            }
            else{
                takingInput = true;
            }
          
    }    
    
    public void act(){

            if (takingInput){


                checkMovement();
                checkActions();
                
            }
          

        if (timeTillAbsorb > 0){
                timeTillAbsorb -= 0.1f;
        }
    }
    
    private void checkMovement(){
        
                 if (Greenfoot.isKeyDown("a")){
                    
                    player.move(-1);


                }
                if (Greenfoot.isKeyDown("d")){
                    
                    player.move(1);

                }
                else{
                    //It seems it must wait for frame until these states are turned off, to register falling to floor.
                    player.getCurrentState().turnOffState(State.MOVINGLEFT);
                    player.getCurrentState().turnOffState(State.MOVINGRIGHT);
                    player.setSpeed(0);
                }
    }
   
    private void checkActions(){
        
        if (!player.getCurrentState().equals(State.JUMPING) && !player.getCurrentState().equals(State.FALLING)){
                    
                    if (Greenfoot.isKeyDown("w")){
                   
                        player.jump();
                    }               
                    //Transformations.
                    else if (Greenfoot.isKeyDown("e")){            
                   
                        if (player.getCurrentState() != PlayerState.ABSORBING && player.getCurrentState() != PlayerState.TRANSFORMING && timeTillAbsorb <= 0){
             
                            player.absorb();    
                        }
                   
                    }                   
                    else if (Greenfoot.isKeyDown("r")){
                        player.revert();

                    }              
                    //Attacking
                    else if (Greenfoot.isKeyDown("f") && player.canAttack()){
                        
                        checkAttack();
                    }
                    
                    //Picking up items
                    else if (Greenfoot.isKeyDown("q")){
                        player.findItem();
                    }
                
                    else if (player.getCurrentState() != PlayerState.DEFAULT){
                    
                        //If it was absorbing, then start ticking the cooldown
                        if (player.getCurrentState() == PlayerState.ABSORBING){
                            timeTillAbsorb = absorbCD;      
                        }
                    
                    //Otherwise if it's not default, then set to default.
                    player.changeState(PlayerState.DEFAULT,false);

                   }
        }
        
    }
    
    private void checkAttack(){
        
          RangedAttack playerAttack = (RangedAttack)playerAttackPools.getReusable(player.getCurrentTransformation() + "attack");
          if (playerAttack == null){
              
          }
          player.attack(playerAttack);  
    }
    
    
    private RangedAttack playerAttackFactory(String attackName){
        
        RangedAttack product = null;
        
        try{
            if (attackName == "fireattack"){
                product =  (RangedAttack)fireAttack.clone();
            }
        }
        catch (Exception e){
            //Log it.
        }
        
        return product;
    }

    
}
