/*
 * MIT License
 *
 * Copyright (c) 2021 Negative
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.negativekb.kitpvpframework.commands.warps;

import dev.negativekb.kitpvpframework.api.KitPvPAPI;
import dev.negativekb.kitpvpframework.api.WarpManager;
import dev.negativekb.kitpvpframework.core.command.Command;
import dev.negativekb.kitpvpframework.core.command.CommandInfo;
import dev.negativekb.kitpvpframework.core.structure.warp.Warp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.negativekb.kitpvpframework.core.Locale.*;

@CommandInfo(name = "deletewarp", aliases = {"delwarp"}, playerOnly = true, permission = "kitpvp.warps.delwarp")
public class CommandDeleteWarp extends Command {

    private final WarpManager warpManager;

    public CommandDeleteWarp() {
        warpManager = KitPvPAPI.getInstance().getWarpManager();

        setTabComplete((sender, args) -> {
            if (args.length == 1) {
                String lastWord = args[args.length - 1];
                List<String> warpNames = new ArrayList<>();
                warpManager.getWarps().stream()
                        .filter(warp -> StringUtil.startsWithIgnoreCase(warp.getName(), lastWord))
                        .forEach(warp -> warpNames.add(warp.getName()));

                return warpNames;
            }

            return null;
        });
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            DELETE_WARP_INVALID_ARGS.send(player);
            return;
        }

        String arg = args[0];
        Optional<Warp> theWarp = warpManager.getWarp(arg);
        if (!theWarp.isPresent()) {
            WARP_COMMAND_INVALID_WARP.replace("%name%", arg).send(player);
            return;
        }

        Warp warp = theWarp.get();
        DELETE_WARP_SUCCESS.replace("%warp%", warp.getName()).send(player);

        warpManager.deleteWarp(warp);
    }
}
