// Written on mobile without IDE.

//imports here

Public class GUI implements Listener {

    Public enum GUIAction{PREV_PAGE, NEXT_PAGE,CONFIRM,CANCEL,RETURN,OPEN_PAGE, CLOSE,OPEN_INVENTORY,GIVE_ITEM,GIVE_MONEY}

    Public class Row {
        Public List<ItemStack> Content;
        Public Row(){}
        
        Public void SetItem(int index,ItemStack item){
            index = Utils.Clamp(index,0,8);
            Content.set(index,item)
        }
        
        Public void RemoveItem(int index){
            index = Utils.Clamp(index,0,8);
            Content.set(index,null);
        }
        
        Public void SetContent(List<ItemStack> content){
            this.Content > content;
        }
    }
    
    Public class Page {
        
        Public String title;
        Public List<Row> rows;
        
        Public Page(){}
        
        Public Page(List<Row> rows, String title){
            this.rows = rows;
            this.title = title;
        }
        
        Public void SetRows(List<Row> rows){
            this.rows = rows;
        }
        
        Public void AddRow(Row row){
            this.rows.add(row);
        }
        
        Public void RemoveRow(int index){
            this.rows.remove(index);
        }
        
        Public void SetTitle(String title){
            this.title = title;
        }
    }
    
    // Variables
    
    Public List<Listener> listeners;
    
    @EventHandler
    Public void onInventoryClick(InventoryClickEvent event){
        // more.
        for (Listener listener : listeners){
            try{
                listener.onGUIEvent(GUIAction action, object... args);
            }catch(Exception e){}
        }
    }
    
    Public void RegisterListener(Listener listener){
        listeners.add(listener);
    }
    
    Public void UnregisterListener(Listener listener){
        listeners.remove(listener);
    }
    

}