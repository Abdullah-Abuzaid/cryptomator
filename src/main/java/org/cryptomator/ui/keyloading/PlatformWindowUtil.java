class PlatformWindowUtil {

  private PlatformWindowUtil() {
    // static utility class
  }

  public static void showWindow(Scene scene,Window window) {
      Platform.runLater(() -> {
			window.setScene(scene);
			window.show();
			Window owner = window.getOwner();
			if (owner != null) {
				window.setX(owner.getX() + (owner.getWidth() - window.getWidth()) / 2);
				window.setY(owner.getY() + (owner.getHeight() - window.getHeight()) / 2);
			} else {
				window.centerOnScreen();
			}
		});
  }
}