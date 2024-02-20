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
package quest.gelkmaros;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Rinzler (Encom)
/****/

public class QUEST_Q70045 extends QuestHandler
{
    private final static int questId = 70045;
	
    public QUEST_Q70045() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(799219).addOnQuestStart(questId);
        qe.registerQuestNpc(799219).addOnTalkEvent(questId);
    }
	
	@Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799219) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
						return sendQuestDialog(env, 1011);
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
			if (targetId == 799219) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 2375);
						} else if (var == 1) {
							return sendQuestDialog(env, 2034);
						}
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 0, 1, true, 5, 2716);
					} case FINISH_DIALOG: {
                        return sendQuestSelectionDialog(env);
                    }
				}
			}
        } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799219) {
                return sendQuestEndDialog(env);
			}
		}
        return false;
    }
}