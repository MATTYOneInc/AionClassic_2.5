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
package quest.morheim;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Rinzler (Encom)
/****/

public class _2392Beautiful_Feather extends QuestHandler
{
	private final static int questId = 2392;
	private int rewardWay = 0;
	
	public _2392Beautiful_Feather() {
		super(questId);
	}
	
	public void register() {
		qe.registerQuestNpc(798085).addOnQuestStart(questId);
		qe.registerQuestNpc(798085).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798085) {
				switch (env.getDialog()) {
                    case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					} case ASK_ACCEPTION: {
						return sendQuestDialog(env, 4);
					} case ACCEPT_QUEST: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			long greenViragoFeather = player.getInventory().getItemCountByItemId(182204159);
			long darkRedCrestlichFeather = player.getInventory().getItemCountByItemId(182204160);
			long whitePeckuMane = player.getInventory().getItemCountByItemId(182204161);
			if (targetId == 798085) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					} case STEP_TO_1: {
						if (greenViragoFeather >= 1) {
							removeQuestItem(env, 182204159, 1);
							changeQuestStep(env, 0, 1, true);
							return sendQuestDialog(env, 5);
						} else {
							return sendQuestDialog(env, 1097);
						}
					} case STEP_TO_2: {
						if (darkRedCrestlichFeather >= 1) {
							removeQuestItem(env, 182204160, 1);
							changeQuestStep(env, 0, 2, true);
							return sendQuestDialog(env, 6);
						} else {
							return sendQuestDialog(env, 1097);
						}
					} case STEP_TO_3: {
						if (whitePeckuMane >= 1) {
							removeQuestItem(env, 182204161, 1);
							changeQuestStep(env, 0, 3, true);
							return sendQuestDialog(env, 7);
						} else {
							return sendQuestDialog(env, 1097);
						}
					}
                }
            }
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			int var = qs.getQuestVarById(0);
			if (targetId == 798085) {
				if (var == 2) {
					rewardWay = 1;
				} else if (var == 3) {
					rewardWay = 2;
				}
				return sendQuestEndDialog(env, rewardWay);
			}
		}
		return false;
	}
}