import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class FirePlayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FirePlayer extends Player
{
    /**
     * Act - do whatever the FirePlayer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    
    public FirePlayer(){
       super();

    }
    
    
    public void act() 
    {
        // Add your action code here.
        super.act();
        //Could add here checking for woddlers I'm strong against so if they collide with me, they die, though I also get hurt?

    }    
    

    public void attack(){
      
    }
    
    
    
    @Override
    public String toString(){
        
        return "firePlayer";
    }
}
