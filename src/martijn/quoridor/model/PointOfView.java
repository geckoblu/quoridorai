package martijn.quoridor.model;

public enum PointOfView {

    POV1, POV2;

    public static PointOfView parse(String pointOfView) {
        pointOfView = pointOfView.toUpperCase();

        return valueOf(pointOfView);
    }

    /*
    @Override
    public String toString() {
        String name = this.name();
        return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }
    */

}
