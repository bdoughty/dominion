package edu.brown.cs.dominion.AI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.dominion.games.Game;

public class TrashTalker {
  private static List<Game> games = new ArrayList<>();
  private static File trashtalk =
      new File("src/main/resources/trash_talk_nice.txt");
  private static Thread spammer;
  private static int RECOVERY_PERIOD = 20000;

  public TrashTalker() {
    spammer = new SpamThread(games);
    spammer.setDaemon(true);
    spammer.start();
  }

  public void addGame(Game g) {
    games.add(g);
  }

  public void removeGame(Game g) {
    games.remove(g);
  }

  private class SpamThread extends Thread {
    private List<Game> tospam;

    public SpamThread(List<Game> gamess) {
      tospam = gamess;
    }

    @Override
    public void run() {
      while (true) {
        for (Game g : tospam) {
          g.sendSpamMessage(getRandomSpamMessage());
        }
        try {
          Thread.sleep(RECOVERY_PERIOD);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static String getRandomSpamMessage() {
    try (BufferedReader br = new BufferedReader(new FileReader(trashtalk))) {
      String out = "go rek urself!!!";
      if (br.ready()) {
        String line = br.readLine();
        int i = 1;
        while (line != null) {
          if (line.equals("")) {
            line = br.readLine();
            continue;
          }
          if (Math.random() * i < 1) {
            out = line;
          }
          line = br.readLine();
          i++;
        }
      }
      return out;
    } catch (Exception e) {
      e.printStackTrace();
      return "you literally broke the trashtalk you're so bad!";
    }
  }
}
