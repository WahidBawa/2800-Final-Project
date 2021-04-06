public class Sounds {

    private static SoundUtilityJOAL soundJOAL;               // needed for sound

    public static void initialSound() {
        soundJOAL = new SoundUtilityJOAL();
        if (!soundJOAL.load("CarBrake", 0f, 0f, 0f, false))
            System.out.println("Could not load " + "CarBrake");

        if (!soundJOAL.load("CarDriving", 0f, 0f, 0f, false))
            System.out.println("Could not load " + "CarDriving");

        if (!soundJOAL.load("CarEngineStart", 0f, 0f, 0f, false))
            System.out.println("Could not load " + "CarEngineStart");

        if (!soundJOAL.load("CarTurn", 0f, 0f, 0f, false))
            System.out.println("Could not load " + "CarTurn");

        if (!soundJOAL.load("Horn", 0f, 0f, 0f, false))
            System.out.println("Could not load " + "Horn");
    }

    /* a function to play different sound according to key (user) */

    /*  CAR BRAKE-0
        CAR DRIVING-1
        CAR ENGINE-2
        CAR TURN-3
        CAR HORN-4
     */
    public static void playSound(int key) {
        String snd_pt = "CarBrake";
        if (key == 1)
            snd_pt = "CarDriving";
        else if (key == 2) {
            snd_pt = "CarEngineStart";
        } else if (key == 3) {
            snd_pt = "CarTurn";
        } else if (key == 4) {
            snd_pt = "Horn";
        }
        soundJOAL.play(snd_pt);
//        try {
//            Thread.sleep(100); // sleep for 0.5 secs
//        } catch (InterruptedException ex) {}
//        soundJOAL.stop(snd_pt);
    }

    public static void stopSounds(int mode) {
        if (mode == 0) {
            soundJOAL.stop("CarBrake");
            soundJOAL.stop("CarDriving");
            soundJOAL.stop("CarEngineStart");
            soundJOAL.stop("CarTurn");
        }
        //this is a special case for the L R buttons to not prematurely stop the blinker sound
        else {
            soundJOAL.stop("CarDriving");
            soundJOAL.stop("CarEngineStart");
            soundJOAL.stop("CarTurn");
        }
    }

}
