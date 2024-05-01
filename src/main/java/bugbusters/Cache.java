package bugbusters;

import org.checkerframework.checker.units.qual.C;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Cache {
    private final int MAX_SIZE = 100;
    private int currentSize = 0;
    private ConcurrentLinkedDeque<PreparedStatement> eQ;   //eviction queue
    private ConcurrentHashMap<PreparedStatement, CacheItem> LRUcache;    //Items will be mostly database search results
    private final long TTL = 180*1000; //TTL=timetolive, three minutes
    public static Cache single_instance = null;

    /**
     * Private Cache constructor
     */
    private Cache() {
        eQ = new ConcurrentLinkedDeque<>();
        LRUcache = new ConcurrentHashMap<>();
        currentSize = 0;
    }

    /**
     * @return single instance of Cache, or new instance if does not exist
     */
    public static Cache getInstance() {
        if(single_instance == null) {
            single_instance = new Cache();
        }
        return single_instance;
    }

    /**
     * Retrieves value for key. If key not found, adds key-value pair to LRUcache and
     * evicts old item. If key found, moves key-value pair to last spot in eviction queue.
     * @param key
     * @return
     */
    public CacheItem getItem(PreparedStatement key) {
        //check if key in cache
        if(LRUcache.containsKey(key)) {
            eQ.remove(key);
            eQ.addLast(key);
            return LRUcache.get(key);
        }

        //otherwise
        CacheItem toReturnAndAddToCache = executeDatabaseCall(key);
        currentSize += toReturnAndAddToCache.getSize();
        eQ.addLast(key);
        LRUcache.put(key, toReturnAndAddToCache);
        while (currentSize > MAX_SIZE) {
            PreparedStatement front = eQ.remove();
            currentSize -= LRUcache.get(front).getSize();
            LRUcache.remove(front);
        }

        return toReturnAndAddToCache;
    }

    private CacheItem executeDatabaseCall(PreparedStatement key) {
        try {
            ResultSet rs = key.executeQuery();
            ArrayList<Course> results = DatabaseSearch.readCourseResults(rs);
            ArrayList<Course> topResults = getTopResults(results);
            CacheItem item = new CacheItem(topResults, topResults.size());
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return new CacheItem(new ArrayList<Course>(), 0);  //returns no search results if error
    }

    private ArrayList<Course> getTopResults(ArrayList<Course> results) {
        int numResults = results.size();
        int numTopResults;
        if (numResults > 10) {
            numTopResults = 10;
        } else {numTopResults = numResults;}

        ArrayList<Course> topResults = new ArrayList<>();

        for (int i = 0; i < numTopResults; i++) {
            topResults.add(results.get(i));
        }
        return topResults;
    }


}














