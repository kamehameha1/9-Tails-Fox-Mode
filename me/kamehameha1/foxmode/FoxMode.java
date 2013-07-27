package me.kamehameha1.foxmode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class FoxMode extends JavaPlugin implements Listener {
  List<Naruto> FoxModeUsers = new ArrayList<Naruto>();
  FoxMode main;

    @Override
  public void onEnable() {
    main = this;
    Bukkit.getPluginManager().registerEvents(this, this);
    Bukkit.getServer().getScheduler()
        .scheduleSyncRepeatingTask(this, new Runnable() {
          private Naruto Naruto;
          public void run() {
            List<Naruto> remove = new ArrayList<Naruto>();
                for (Iterator<Naruto> it = FoxModeUsers.iterator(); it.hasNext();) {
                    Naruto  = it.next();
                    Player p = Bukkit.getPlayerExact(Naruto.getPlayer());
                    if (p == null) {
                      remove.add(Naruto);
                      continue;
                    }
                    Naruto.decreaseFoxMode();
                    Naruto.decreaseKyuubi();
                    Naruto.decreasePowerwave();
                    if (Naruto.getKyuubiTime() <= 0 && Naruto.getKyuubi()) {
                      Bukkit.broadcastMessage(ChatColor.DARK_RED + p.getName()
                          + "'s mask breaks away..");
                      Naruto.setKyuubi(false);
                    }
                    if ((Naruto.getKyuubi() || Naruto.getFoxModeTime() % 1 == 0)
                        && p.getHealth() < 20) {
                          p.setHealth(p.getHealth() + 1);
                      }
                    if (Naruto.getPowerwave() == 0) {
                          p.sendMessage(ChatColor.DARK_RED
                              + "Your Rasen-Shruken Is Ready!!");
                      }
                    if (Naruto.getKyuubiTime() <= 0 && Naruto.getKyuubi()) {
                      Bukkit.broadcastMessage(ChatColor.DARK_RED + p.getName()
                          + "'s power fades away...");
                      Naruto.setKyuubi(false);
                    }
                    if (Naruto.getFoxModeTime() <= 0) {
                      remove.add(Naruto);
                      p.getWorld().strikeLightningEffect(p.getLocation());
                      Bukkit.broadcastMessage(ChatColor.DARK_RED
                          + "The surge of power surrounding " + Naruto.getPlayer()
                          + " disappear..");
                      continue;
                    } else {
                      if (new Random().nextInt(3) == 1) {
                            p.getWorld().playEffect(p.getEyeLocation(),
                                Effect.MOBSPAWNER_FLAMES, 0);
                        }
                      if (!Naruto.getKyuubi()) {
                            p.addPotionEffect(new PotionEffect(
                                PotionEffectType.REGENERATION, 10, 0), true);
                        }
                      else {
                            p.addPotionEffect(new PotionEffect(
                                PotionEffectType.REGENERATION, 10, 5), true);
                        }
                      p.addPotionEffect(new PotionEffect(
                          PotionEffectType.FIRE_RESISTANCE, 10, 0), true);
                      if (!Naruto.getKyuubi()) {
                            boolean addPotionEffect = p.addPotionEffect(new PotionEffect(
                                                          PotionEffectType.INCREASE_DAMAGE, 10, 2), true);
                        }
                      else {
                            p.addPotionEffect(new PotionEffect(
                                PotionEffectType.INCREASE_DAMAGE, 10, 5), true);
                        }
                      if (Naruto.getKyuubi()) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10,
                                0), true);
                        }
                      p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10,
                          2), true);
                      if (Naruto.getFoxModeTime() % 1 == 0) {
                        Location loc = p.getLocation();
                        int z = -2;
                        int x = -2;
                        for (int i = 0; i < 25; i++) {
                          Location newLoc = new Location(loc.getWorld(), loc.getX()
                              + x, loc.getY(), loc.getZ() + z);
                          for (int direction = 0; direction < 8; direction++) {
                            loc.getWorld()
                                .playEffect(newLoc, Effect.SMOKE, direction);
                          }
                          if (x == 2) {
                            x = 0;
                            z++;
                          }
                          x++;
                        }
                      }
                    }
                }
            FoxModeUsers.removeAll(remove);
          }
        }, 0, 1L);
  }

  Naruto getNaruto(String name) {
    for (Naruto Naruto : FoxModeUsers) {
      if (Naruto.getPlayer().equals(name)) {
            return Naruto;
        }
    }
    return null;
  }

  @EventHandler
  public void onDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      Player p = (Player) event.getEntity();
      Naruto Naruto = getNaruto(p.getName());
      if (Naruto != null) {
            event.setDamage(Math.round((event.getDamage() / 3) * 2));
        }
    }
  }

  @EventHandler
  public void playerInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_AIR) {
          return;
      }
    Player p = event.getPlayer();
    Naruto Naruto = getNaruto(p.getName());
    if (Naruto == null) {
          return;
      }
    if (Naruto.getPowerwave() > 0) {
          return;
      }
    if (p.getItemInHand().getType() != Material.IRON_SWORD) {
          return;
      }
    Bukkit.broadcastMessage(ChatColor.DARK_RED + Naruto.getPlayer()
        + " screams Rasen-Shuriken!");
    final Vector direction = p.getEyeLocation().getDirection().multiply(2);
    final Fireball fireball = p.getWorld().spawn(
        p.getEyeLocation().add(direction.getX(), direction.getY(),
            direction.getZ()), Fireball.class);
    fireball.setShooter(p);
    fireball.setIsIncendiary(true);
    if (Naruto.getKyuubi()) {
          fireball.setYield(20);
      }
    else {
          fireball.setYield(10);
      }
    Naruto.setPowerwave(17);
  }

  @EventHandler
  public void onDeath(EntityDeathEvent event) {
    if (event.getEntity() instanceof Player) {
      Player p = (Player) event.getEntity();
      Naruto Naruto = getNaruto(p.getName());
      if (Naruto != null) {
            FoxModeUsers.remove(Naruto);
        }
    }
  }

    @Override
  public boolean onCommand(CommandSender sender, Command cmd,
      String commandLabel, String[] args) {
    Player player = Bukkit.getPlayerExact(sender.getName());
    if (player == null) {
              return true;
          }
    if (cmd.getName().equalsIgnoreCase("FoxMode")) {
      if (player.isOp() || player.hasPermission("FoxMode.use")) {
        if (args.length == 1
            && (player.isOp() || player
                .hasPermission("FoxMode.others"))) {
          player = Bukkit.getPlayer(args[0]);
          if (player == null) {
            sender
                .sendMessage(ChatColor.RED + "That Player Is Offline!");
            return true;
          } else {
                        player.sendMessage(ChatColor.RED + sender.getName()
                            + " Just Helped You Go In To 9 Tails Fox Mode!");
                    }
        }
        final Player p = player;
        Naruto Naruto = getNaruto(player.getName());
        if (Naruto == null) {
          player.addPotionEffect(
              new PotionEffect(PotionEffectType.SLOW, 60, 2), true);
          Bukkit.broadcastMessage("<" + p.getDisplayName() + ChatColor.RESET
              + "> " + ChatColor.DARK_RED + "Gah");
          Bukkit.getServer().getScheduler()
              .scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                  Bukkit.broadcastMessage(ChatColor.DARK_RED + "* "
                      + ChatColor.stripColor(p.getDisplayName())
                      + ChatColor.RESET + ChatColor.DARK_RED + " grunts with effort");
                }
              }, 20L);
          Bukkit.getServer().getScheduler()
              .scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                  Bukkit.broadcastMessage("<" + p.getDisplayName()
                      + ChatColor.RESET + "> " + ChatColor.DARK_RED + "You Can't Get ME Fox!");
                  if (new Random().nextInt(5) != 1) {
                    Bukkit.broadcastMessage(ChatColor.DARK_RED
                        + "The 9 Tail Fox's Power Consumes " + p.getName() + "...");
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    for (int n = 0; n <= 3; n++) {
                      Bukkit.getServer().getScheduler()
                          .scheduleSyncDelayedTask(main, new Runnable() {
                            public void run() {
                              p.getWorld().strikeLightningEffect(
                                  p.getLocation());
                            }
                          }, n * 2L);
                    }
                    Naruto Naruto = new Naruto(p.getName());
                    FoxModeUsers.add(Naruto);
                  } else {
                    Bukkit.broadcastMessage(ChatColor.DARK_RED
                        + "Dark Forces Envelope " + p.getName() + "!");
                    Bukkit
                        .broadcastMessage(ChatColor.DARK_RED
                            + "The Dark Forces Go Out Of Control And Attacks "
                            + p.getName() + "!");
                    p.damage(1000000);
                  }
                }
              }, 40L);
        } else {
              sender.sendMessage(ChatColor.RED + "You Already In Bankai Dude!");
          }
      }
    }
    if (cmd.getName().equalsIgnoreCase("Kyuubi")) {
      final Naruto Naruto = getNaruto(sender.getName());
      if (Naruto == null) {
        sender.sendMessage(ChatColor.RED + "You Aren't In FoxMode Use /foxmode!");
        return true;
      }
      if (Naruto.getKyuubi()) {
        sender.sendMessage(ChatColor.RED + "Already In Kyuubi State!");
        return true;
      }
      Bukkit.broadcastMessage(ChatColor.DARK_RED + sender.getName()
          + " Suddenly Turned Into The 9 Tails Fox!");
      player.getWorld().strikeLightning(player.getLocation());
      if (new Random().nextInt(3) == 1 || Naruto.getFoxModeTime() < 10
          || Naruto.hasKyuubied()) {
        final Player p = player;
        Bukkit.getServer().getScheduler()
            .scheduleSyncDelayedTask(main, new Runnable() {
              public void run() {
                Bukkit.broadcastMessage(ChatColor.DARK_RED
                    + p.getName()
 
                  + " The Fox Takes Over....");
                FoxModeUsers.remove(Naruto);
                p.damage(999999);
              }
            }, 20L);
        return true;
      } else {
        Naruto.setKyuubiTime(10);
        Naruto.setKyuubi(true);
      }
    }
    return true;
  }
}
