using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Labb2_Knapsack {

    class Problem {

        static void Main(string[] ags) {
            int amountOfKnaps, amountOfItems, knapSize;
            string[] itemName; //Kan tas bort med finns lite för att det blir roligare och för att garantera att maxItemWeight > maxKnapWeight
            List<Knapsack> greedySackList; //För greedy algorithm
            List<Knapsack> hoodSackList; //För neighborhood search algorithm
            List<Item> itemList; //Alla våra items
            
            amountOfKnaps = 2; //Hur många knaps vi vill ha
            amountOfItems = 10; //Hur många items vi vill ha
            knapSize = 10; //Hur mycket vikt varje knap kan hålla

            greedySackList = new List<Knapsack>();
            hoodSackList = new List<Knapsack>();
            itemList = new List<Item>();
            itemName = new string[] { "axe", "pillow", "drugz", "computer", "nice rock", "ToroToro" };

            greedySackList = CreateKnapsacks(amountOfKnaps, knapSize); //Skapar greedySackList
            hoodSackList = CreateKnapsacks(amountOfKnaps, knapSize); //Skapar hooSackList (Anledningen för att vara uppdelade är för att de ska jämföras, hood måste vara bättre eller lika bra som greedy!
            itemList = CreateItems(amountOfItems, itemName); //Skapar items

            Greedy(greedySackList, itemList); //Ska fylla greedySackList enligt en Greedy Algorithm
            //Hood(hoodSackList, itemList);

            PrintResult(itemList, greedySackList, hoodSackList);

            Console.WriteLine("Press any key to exit");
            Console.ReadKey();
        }

        public static List<Knapsack> CreateKnapsacks(int amountOfKnaps, int knapSize) {
            List<Knapsack> knapList = new List<Knapsack>();
            for (int i = 0; i < amountOfKnaps; i++) {
                Knapsack knap = new Knapsack(knapSize);
                knapList.Add(knap);
            }
            return knapList;
        }

        public static List<Item> CreateItems(int amountOfItems, string[] itemName) {
            List<Item> itemList = new List<Item>();
            Item item;
            for (int i = 0; i < amountOfItems; i++) {
                
                if (i < itemName.Length) { //Om vi har flera items än vad jag har orkat ge namn till blir det ett vanligt Item
                    item = new Item(itemName[i]);
                } else {
                    item = new Item("Item("+ (i + 1) +")");
                }
                itemList.Add(item);
                
            }
            return itemList;
        }

        public static void Greedy(List<Knapsack> greedyKnap, List<Item> items) { //Greedy Algorithm
            List<Item> greedyItem = new List<Item>(); //För att se till att vi inte behöver återställa items
            List<Item> greedyItemLeft = new List<Item>();
            foreach (Item item in items) {
                greedyItem.Add(item);
            }

            int itemA = 0; //Första item i listan
            int itemB = 1; //Andra item i listan (Eftersom items tas bort stämmer detta alltid)

            bool isDone = false;

            while (!isDone) {
                
                //Kollar ifall det finns mer att göra
                if (CheckIfDone(greedyKnap, greedyItem)) {
                    isDone = true;
                    break;
                }
                
                //Själva greedy Algorithm
                if (greedyItem.Count > 1) { //Im det inte finns flere än 1 item 
                    for (int i = 0; i < greedyItem.Count; i++) {
                        if (greedyItem[itemA].huValue >= greedyItem[itemB].huValue) { //Kollar vilket  av items som är bäst
                            if (!AddItem(greedyKnap, greedyItem[itemA])) { //Lägger till item om det går
                                greedyItemLeft.Add(greedyItem[itemA]); //Om det inte går hamnar items i greedyItemLeft
                            }
                            greedyItem.Remove(greedyItem[itemA]); //Tar bort item så att det inte testas igen

                        } else if (greedyItem[itemA].huValue < greedyItem[itemB].huValue) {
                            if(!AddItem(greedyKnap, greedyItem[itemB])) {
                                greedyItemLeft.Add(greedyItem[itemB]);
                            }
                            greedyItem.Remove(greedyItem[itemB]);
                        }
                    }
                } else {
                    if (!AddItem(greedyKnap, greedyItem[itemA])) {
                        greedyItemLeft.Add(greedyItem[itemA]);
                    }
                    greedyItem.Remove(greedyItem[itemA]);
                }
                
                

            //if (item1 > item2) then we add item1 and we keep going throught the list testing item2 to the next ones.
            //else if (item1 < item2) the we add item2 and we keep going through the list testing item1 to the next ones

            //I got the "best" item... now where do I put it?
            // - Randomly
            // - The one wiht the less space -> without going over the limit
            // - Other solutions
            }
        }

        public static void Hood(List<Knapsack> hoodKnap, List<Item> items) {
            List<Item> hoodItem = items;
            bool isDone = false;

            while (!isDone) {
                //Kollar ifall det finns mer att göra
                if(CheckIfDone(hoodKnap, hoodItem)) {
                    break;
                }

                //Själva hood Algorithm
            }
        }

        public static bool AddItem(List<Knapsack> knapSack, Item item) {
            List<Knapsack> knapList = knapSack;
            bool isPlaced = false;
            int sizeLeft = int.MinValue;
            Knapsack bestKnap = knapList[0];

            foreach (Knapsack knap in knapList) {
                if (knap.maxWeight - knap.totWeight >= sizeLeft) {
                    bestKnap = knap;
                    sizeLeft = (knap.maxWeight - knap.totWeight);
                }
            }

            foreach (Knapsack knap in knapList) {
                if (knap == bestKnap) {
                    isPlaced = knap.AddItem(item);
                    break;
                }
            }
            
            return isPlaced;

        }

        public static bool CheckIfDone(List<Knapsack> knapSack, List<Item> item) {
            bool isDone = false;
            int knapsWithPlace = 0;
            foreach (Knapsack knap in knapSack) {
                if (knap.totWeight < knap.maxWeight) {
                    knapsWithPlace++;
                }
            }
            if (knapsWithPlace <= 0) {
                isDone = true;
            }
            if (item.Count == 0) {
                isDone = true;
            }

            return isDone;
        }

        public static void PrintResult(List<Item> itemList, List<Knapsack> greedySackList, List<Knapsack> hoodSackList) {
            int totItemValue = 0;
            int totItemWeight = 0;
            Console.WriteLine("\n----------------------------------------\n");

            Console.WriteLine("The items(" + itemList.Count + ") that have to be desputed:");
            foreach (Item item in itemList) {
                Console.WriteLine("Item: " + item.itemName + ", value: " + item.itemValue + ", weight: " + item.itemWeight + ", huValue: " + item.huValue);
                totItemValue += item.itemValue;
                totItemWeight += item.itemWeight;
            }
            Console.WriteLine("\nTotal item value: " + totItemValue + "\nTotal item weight: " + totItemWeight);

            Console.WriteLine("\n----------------------------------------\n");

            foreach (Knapsack greedyKnap in greedySackList) {
                Console.WriteLine("The GreedyKnap contains: " + greedyKnap.itemsInKnap.Count + " items\nTotal value: " + greedyKnap.totValue + "\nTotal weight: " + greedyKnap.totWeight + "\n");
                Console.WriteLine("This knap contains these items:");
                foreach (Item item in greedyKnap.itemsInKnap) {
                    Console.WriteLine("Item: " + item.itemName + ", value: " + item.itemValue + ", weight: " + item.itemWeight + ", huValue: " + item.huValue);
                }
                Console.WriteLine("\n----------------------------------------\n");
            }

            foreach (Knapsack hoodKnap in hoodSackList) {
                Console.WriteLine("The HoodKnap contains: " + hoodKnap.itemsInKnap.Count + " items\nTotal value: " + hoodKnap.totValue + "\nTotal weight: " + hoodKnap.totWeight + "\n");
                Console.WriteLine("This knap contains these items:");
                foreach (Item item in hoodKnap.itemsInKnap) {
                    Console.WriteLine("Item: " + item.itemName + ", value: " + item.itemValue + ", weight: " + item.itemWeight + ", huValue: " + item.huValue);
                }
                Console.WriteLine("\n----------------------------------------\n");
            }
        }
    }
}
