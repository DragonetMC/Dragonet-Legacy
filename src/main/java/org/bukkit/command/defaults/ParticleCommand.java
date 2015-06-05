package org.bukkit.command.defaults;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParticleCommand extends VanillaCommand {
    public ParticleCommand() {
        super("particle");
        this.description = "Creates particle effects.";
        this.usageMessage = "/particle <name> <x> <y> <z> <xd> <yd> <zd> <speed> [count] [mode]";
        this.setPermission("bukkit.command.particle");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        if (args.length < 8) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You can only perform this command as a player");
            return true;
        }

        final Player player = (Player) sender;

        final Location location = player.getLocation();
        final World world = location.getWorld();

        final String name = args[0];
        final float x = (float) getRelativeDouble(location.getX(), sender, args[1]);
        final float y = (float) getRelativeDouble(location.getY(), sender, args[2]);
        final float z = (float) getRelativeDouble(location.getZ(), sender, args[3]);
        final float xd = (float) getDouble(sender, args[4]);
        final float yd = (float) getDouble(sender, args[5]);
        final float zd = (float) getDouble(sender, args[6]);
        final float speed = (float) getDouble(sender, args[7]);
        final int count = args.length > 8 ? getInteger(sender, args[8], 1) : 1;
        final boolean force = args.length > 9 ? args[9].equals("force") : false;

        Effect effect = Effect.getByName(name);
        if (effect == null || effect.getType() != Effect.Type.PARTICLE) {
            sender.sendMessage("Unknown particle effect: " + name);
            return true;
        }

        int id = effect.getId();
        int data = 0; // TODO
        int radius = force ? 48 : 16;

        world.spigot().playEffect(new Location(world, x, y, z), effect, id, data, xd, yd, zd, speed, count, radius);

        return true;
    }
}
