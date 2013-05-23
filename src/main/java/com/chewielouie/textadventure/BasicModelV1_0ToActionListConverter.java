package com.chewielouie.textadventure;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class BasicModelV1_0ToActionListConverter {
    private TextAdventureModel oldModel;
    private TextAdventureModel newModel;
    private UserInventory inventory;
    private ActionFactory actionFactory;
    private List<Action> actions;

    public BasicModelV1_0ToActionListConverter( TextAdventureModel oldModel,
                TextAdventureModel newModel, UserInventory inventory,
                ActionFactory factory ) {
        this.oldModel = oldModel;
        this.newModel = newModel;
        this.inventory = inventory;
        this.actionFactory = factory;
    }

    public List<Action> actions() {
        // inspect the state of the model and fill in the action history with guesses
            // 1. Figure out what has been picked up
                // a. if skeleton key is in inventory do 'take specific item:clocktowerskeletonkey:townentrance'
                // b... if xxx is in inventory do 'take specific item:item id:location id'
            // 2. Figure out what has been used
            // 3. Figure out what has been examined
            // 4. Figure out where the player is and get them there by using exits
        actions = new ArrayList<Action>();

        analyseImmediatelyTakeableItems();
        addUseKeyActionIfDoorIsUnlocked();
        generateClockFaceLifetimeActions();

        return actions;
    }

    private void analyseImmediatelyTakeableItems() {
        addTakeActionIfItemHasBeenPickedUp( "clocktowerskeletonkey", "townentrance" );
        addTakeActionIfItemHasBeenPickedUp( "bananapeel", "townentrance" );
        addTakeActionIfItemHasBeenPickedUp( "dustoftheancients", "mainstreettown" );
        addTakeActionIfItemHasBeenPickedUp( "spade", "smallshed" );
    }

    private void addTakeActionIfItemHasBeenPickedUp( String itemId, String locationId ) {
        if( itemIsInOldInventory( itemId ) )
            addTakeAction( itemId, locationId );
    }

    private void addUseKeyActionIfDoorIsUnlocked() {
        Item lockedDoor = oldModel.findItemByID( "lockeddoor" );
        if( lockedDoor != null && lockedDoor.name().equals( "unlocked door" ) )
            addUseAction( "lockeddoor", "clocktowerskeletonkey" );
    }

    private void generateClockFaceLifetimeActions() {
        if( itemIsInLocation( "clockface", "townoutbuildings" ) )
            addUseAction( "moundofearth", "spade" );

        if( itemIsInOldInventory( "clockface" ) ) {
            addUseAction( "moundofearth", "spade" );
            addTakeAction( "clockface", "townoutbuildings" );
        }

        Item mechanism = oldModel.findItemByID( "clockmechanismwithface" );
        if( mechanism != null && mechanism.visible() ) {
            addUseAction( "moundofearth", "spade" );
            addTakeAction( "clockface", "townoutbuildings" );
            addUseAction( "clockmechanism", "clockface" );
        }
    }

    private void addTakeAction( String itemId, String locationId ) {
        actions.add( actionFactory.createTakeSpecificItemAction(
                                        findNewModelItem( itemId ),
                                        inventory,
                                        findNewModelLocation( locationId ) ) );
    }

    private void addUseAction( String actionOwnerItemID, String targetItemID ) {
        actions.add( actionFactory.createUseWithSpecificItemAction(
                                        findNewModelItem( actionOwnerItemID ),
                                        findNewModelItem( targetItemID ) ) );
    }

    private Item findNewModelItem( String id ) {
        return newModel.findItemByID( id );
    }

    private ModelLocation findNewModelLocation( String id ) {
        return newModel.findLocationByID( id );
    }

    private boolean itemIsInOldInventory( String id ) {
        for( Item item : oldModel.inventoryItems() )
            if( item.id().equals( id ) )
                return true;
        return false;
    }

    private boolean itemIsInLocation( String itemID, String locationID ) {
        Item item = oldModel.findItemByID( itemID );
        ModelLocation location = oldModel.findLocationByID( locationID );
        if( location != null && item != null && location.items().contains( item ) )
            return true;
        return false;
    }
}
