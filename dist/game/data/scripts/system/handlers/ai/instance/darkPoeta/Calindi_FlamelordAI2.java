/*
 *  Aion Classic Emu based on Aion Encom Source Files
 *
 *  ENCOM Team based on Aion-Lighting Open Source
 *  All Copyrights : "Data/Copyrights/AEmu-Copyrights.text
 *
 *  iMPERIVM.FUN - AION DEVELOPMENT FORUM
 *  Forum: <http://https://imperivm.fun/>
 *
 */
package ai.instance.darkPoeta;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.commons.network.util.ThreadPoolManager;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.WorldMapInstance;

import java.util.*;

/****/
/** Author Rinzler (Encom)
/****/

@AIName("Calindi_Flamelord")
public class Calindi_FlamelordAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	private int curentPercent = 100;
	private List<Integer> percents = new ArrayList<Integer>();
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[]{100, 50, 30, 15, 10});
	}
	
	private synchronized void checkPercentage(int hpPercentage) {
		curentPercent = hpPercentage;
		for (Integer percent: percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 100:
					    ///How were you able to come all the way here with such meager strength? I do not find you worth my while any longer!
						sendMsg(341870, getObjectId(), false, 0);
						///Since you reached this far, I'll grant you the honor of facing me. But I will leave immediately if this honor is too much for you to bear.
						sendMsg(341847, getObjectId(), false, 5000);
						///You wretched Daeva! I shall punish you and send you to meet death face to face!
						sendMsg(341848, getObjectId(), false, 10000);
						///The only thing that awaits you wretches now is hell.
						sendMsg(341849, getObjectId(), false, 15000);
					break;
					case 50:
					case 30:
					case 10:
					    switch (Rnd.get(1, 2)) {
							case 1:
								faithfulSubordinate1();
							break;
							case 2:
								faithfulSubordinate2();
							break;
						}
						///I will show you the might of the Fire Dragon.
						sendMsg(341827, getObjectId(), false, 0);
						///Cleave the earth, and rise up!
						sendMsg(341829, getObjectId(), false, 4000);
						///You shall turn into old bones buried in the ground!
						sendMsg(341831, getObjectId(), false, 8000);
					break;
					case 15:
					    AI2Actions.useSkill(this, 18232); //Explosion Of Wrath.
					break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
	
	private void faithfulSubordinate1() {
		//Rise, My Faithful Servant.
		AI2Actions.useSkill(this, 18233);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rndSpawn(281258, 3);
			}
		}, 3500);
	}
	
	private void faithfulSubordinate2() {
		//Rise, My Faithful Servant.
		AI2Actions.useSkill(this, 18233);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rndSpawn(281259, 3);
			}
		}, 3500);
	}
	
	private void rndSpawn(int npcId, int count) {
		for (int i = 0; i < count; i++) {
			SpawnTemplate template = rndSpawnInRange(npcId, 8);
			SpawnEngine.spawnObject(template, getPosition().getInstanceId());
		}
	}
	
	protected SpawnTemplate rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x = (float) (Math.cos(Math.PI * direction) * distance);
        float y = (float) (Math.sin(Math.PI * direction) * distance);
		return SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, getPosition().getX() + x, getPosition().getY() + y, getPosition().getZ(), getPosition().getHeading());
	}
	
	private void killNpc(List<Npc> npcs) {
		for (Npc npc: npcs) {
			AI2Actions.killSilently(this, npc);
		}
	}
	
	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		percents.clear();
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}
	
	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		addPercent();
		canThink = true;
		curentPercent = 100;
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		killNpc(instance.getNpcs(281258));
		killNpc(instance.getNpcs(281259));
	}
	
	@Override
	protected void handleDied() {
		super.handleDied();
		percents.clear();
		getOwner().getEffectController().removeAllEffects();
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		killNpc(instance.getNpcs(281258));
		killNpc(instance.getNpcs(281259));
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}