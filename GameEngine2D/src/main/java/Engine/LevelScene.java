package Engine;

public class LevelScene extends Scene{

    public LevelScene(){
        System.out.println("Dentro de level scene");
        Ventana.get().r = 1.0f;
        Ventana.get().g = 1.0f;
        Ventana.get().b = 1.0f;
    }

    @Override
    public void update(float dt) {

    }
}
