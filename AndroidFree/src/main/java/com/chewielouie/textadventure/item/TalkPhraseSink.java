package com.chewielouie.textadventure.item;

import com.chewielouie.textadventure.itemaction.ItemAction;
import java.util.List;

public interface TalkPhraseSink {
    public void addInitialPhrase( String id, String content );
    public void addResponse( String id, String response );
    public void addFollowUpPhrase( String parentId, String newPhraseId, String content );
    public void addActionInResponseTo( String id, ItemAction action );
}
