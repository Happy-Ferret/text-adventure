package com.chewielouie.textadventure;

import java.util.List;

public interface ModelLocation {
    public void addExit( String exitLabel, String destinationId );
    public boolean exitable( Exit exit );
    public String exitDestinationFor( Exit exit );
    public String id();
    public List<Exit> exits();
    public String description();
}


