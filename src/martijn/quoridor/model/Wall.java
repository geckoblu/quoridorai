package martijn.quoridor.model;

public enum Wall {

	HORIZONTAL, VERTICAL;

	public Wall flip() {
		return values()[(ordinal() + 1) % 2];
	}

}
