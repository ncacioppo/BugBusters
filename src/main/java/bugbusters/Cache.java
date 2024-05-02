package bugbusters;

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
        PreparedStatement LRU_key = getKeyFromLRUcache(key);
        if(LRU_key != null) {
            delayKeyInEQ(key);  //get other key
            return LRUcache.get(LRU_key);
        }

        //otherwise
        ArrayList<Course> results = executeDatabaseCall(key);
        CacheItem toReturn = new CacheItem(results, results.size());
        ArrayList<Course> topResults = getTopResults(results);
        CacheItem toAddToCache = new CacheItem(topResults, topResults.size());

        currentSize += toAddToCache.getSize();
        eQ.addLast(key);
        LRUcache.put(key, toAddToCache);
        while (currentSize > MAX_SIZE) {
            PreparedStatement front = eQ.remove();
            currentSize -= LRUcache.get(front).getSize();
            LRUcache.remove(front);
        }

        return toReturn;
    }

    private void delayKeyInEQ(PreparedStatement ps) {
        for(PreparedStatement eQ_key : eQ) {
            if(eQ_key.toString().equals(ps.toString())) {
                eQ.remove(eQ_key);
                eQ.addLast(eQ_key);
                break;
            }
        }
    }

    private PreparedStatement getKeyFromLRUcache(PreparedStatement ps) {
        for(PreparedStatement LRU_key : LRUcache.keySet()) {
            if(LRU_key.toString().equals(ps.toString())) {
                return LRU_key;
            }
        }
        return null;
    }

    private ArrayList<Course> executeDatabaseCall(PreparedStatement key) {
        try {
            ResultSet rs = key.executeQuery();
            return DatabaseSearch.readCourseResults(rs);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<Course>();  //returns no search results if error
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

    /**
     * Currently called after executing a search query in DatabaseSearch.executeQuery()
     */
    public void checkAndRefresh() {
        long currentTime = System.currentTimeMillis();
        long timeAdded;
        for (PreparedStatement key : LRUcache.keySet()) {
            timeAdded = LRUcache.get(key).getTimeAdded();
            if (currentTime - timeAdded >= TTL) {
                LRUcache.remove(key);
                eQ.remove(key);
            }
        }
    }

    public void clearCache() {
        eQ = new ConcurrentLinkedDeque<>();
        LRUcache = new ConcurrentHashMap<>();
        currentSize = 0;
    }


}














