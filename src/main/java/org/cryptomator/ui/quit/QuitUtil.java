class QuitUtil{
  
  private QuitUtil() {
    // utility class
  }

 private void lockAndQuit(Button lockAndQuitButton,Collection<Vault> vaults, 
    Window window, 
    
    ExecutorService executorService, boolean forceQuit,)
  {


    lockAndQuitButton.setDisable(true);
    lockAndQuitButton.setContentDisplay(ContentDisplay.LEFT);
    Task<Collection<Vault>> lockAllTask = vaultService.createLockAllTask(vaults, false);
    lockAllTask.setOnSucceeded(evt -> {
        LOG.info("Locked {}", lockAllTask.getValue().stream().map(Vault::getDisplayName).collect(Collectors.joining(", ")));
        if (vaults.isEmpty()) {
            window.close();
            respondToQuitRequest(QuitResponse::performQuit);
        }
    });
    lockAllTask.setOnFailed(evt -> {
        LOG.warn(forceQuit ? "Forced locking failed" : "Locking failed", lockAllTask.getException());
        if (forceQuit) {
            forceLockAndQuitButton.setDisable(false);
            forceLockAndQuitButton.setContentDisplay(ContentDisplay.TEXT_ONLY);

            window.close();
            respondToQuitRequest(QuitResponse::cancelQuit);
        } else {
            window.setScene(quitForcedScene.get());
        }
        
    });
    executorService.execute(lockAllTask);
  }
}