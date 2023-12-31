/* Stephanie Sandoval - Caso 02
 * OrderMaker - Class in charge of preparing the orders
 * Basically, the one that puts the bouquets and amounts in a single object
 * And calculates the prices of the flowers based on the catalogue information
 */

package Orders;

import java.util.ArrayList;

import Flowers.Bouquet;
import Flowers.Catalogue;
import Flowers.Flower;
import Packages.Prepackage;

public class OrderMaker {
    private ArrayList <Bouquet> bouquets;
    private ArrayList <Float> prices;
    private Catalogue catalogue;
    private Order order;
    private Prepackage prepackage;

    private ArrayList <ArrayList <Flower>> flowers;
    private ArrayList <ArrayList <Integer>> amounts;
    private ArrayList <ArrayList <Flower>> availableFlowers;
    private ArrayList <ArrayList <Integer>> availableAmounts;

    public OrderMaker (Order order){
        this.order = order;
        this.bouquets = new ArrayList<>();
        this.prices = new ArrayList<>();
        this.availableFlowers = new ArrayList<>();
        this.availableAmounts = new ArrayList<>();
        this.catalogue = Catalogue.getInstance();
        processOrder();
        createBouquets();
        calculatePrices();
    }

    private void processOrder (){
        // get the flowers the user asked for and the amounts
        // remember a bouquet can contain flowers of different types
        this.flowers = order.getFlowers();
        this.amounts = order.getAmounts();
    }
    
    private void createBouquets (){
        Bouquet newBouquet;
        for (ArrayList <Flower> flowerList : flowers){
            ArrayList <Integer> amountList = amounts.get(flowers.indexOf(flowerList));
            checkFlowers(flowerList, amountList);
        }
        for (ArrayList <Flower> flowers : availableFlowers){
            newBouquet = new Bouquet(availableFlowers.get(availableFlowers.indexOf(flowers)), availableAmounts.get(availableFlowers.indexOf(flowers)));
            bouquets.add(newBouquet);
        }
    }

    private void checkFlowers (ArrayList <Flower> flowerList, ArrayList <Integer> amountList){
        ArrayList <Flower> available = new ArrayList<>();
        ArrayList <Integer> availableAmount = new ArrayList<>();
        for (Flower flower : flowerList){
            if (catalogue.contains(flower)){
                available.add(flower);
                availableAmount.add(amountList.get(flowerList.indexOf(flower)));
            }
        }
        availableFlowers.add(available);
        availableAmounts.add(availableAmount);
    }

    private void calculatePrices (){
        ArrayList <Flower> flowerList;
        float bouquetPrice;
        for (Bouquet bouquet : bouquets){
            bouquetPrice = 0;
            flowerList = bouquet.getBouquet();
            for (Flower flower : flowerList){
                // add to total price the price of the flower multiplied by the amount of them
                bouquetPrice += catalogue.getPrice(flower) * amounts.get(bouquets.indexOf(bouquet)).get(flowerList.indexOf(flower));
            }
            prices.add(bouquetPrice);
        }
    }

    private void createPrepackage (){
        this.prepackage = new Prepackage(bouquets, prices);
    }

    public Prepackage getPrepackage (){
        createPrepackage();
        return this.prepackage;
    }

    public boolean isComplete (){
        return (flowers.size() == availableFlowers.size());
    }
}