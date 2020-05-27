package com.example.battleships;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;


public class CAdapter extends BaseAdapter  implements View.OnDragListener  {

    public boolean shipsVisible = true;

    Game game;
    Context context;
    int currentDragId = -1;

    CAdapter(Context context, Game game){
        this.game = game;
        this.context = context;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public int getCount() {
        return Constants.BOARD_SIZE;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View cell;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cell = inflater.inflate(R.layout.cell_item, parent, false);
        final ImageButton btn = cell.findViewById(R.id.cellBtn);

         switch (game.getCellStatusById(position)){
            case UNCOVERED:
            case BUSY:
                btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                if(shipsVisible && game.board.getShipById(position) != null)
                    btn.setBackgroundColor(context.getResources().getColor(R.color.colorPreview));
                break;
            case HIT:
                btn.setForeground(context.getResources().getDrawable(R.drawable.ic_check_black_24dp));
                btn.setBackgroundColor(context.getResources().getColor(R.color.colorCheck));
                break;
            case MISS:
                btn.setForeground(context.getResources().getDrawable(R.drawable.ic_x_black_24dp));
                btn.setBackgroundColor(context.getResources().getColor(R.color.colorX));
                break;
            case DROWNED:
                btn.setForeground(context.getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                btn.setBackgroundColor(context.getResources().getColor(R.color.colorDrowned));
                break;
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!game.setupDone){

                    Ship ship = game.board.getShipById(((GridView) v.getParent().getParent()).getPositionForView(v));
                    Log.d("ROTATE", "Ship clicked with id " + ship.getFirstId());

                    if(ship != null){
                        game.setShipSelected(ship);
                        Log.d("ROTATE", "Ship clicked with id " + ship.getFirstId() + "NEXT STEP");
                    }

                    return;
                }
                game.updateCellStatusOnClicked(position);
                notifyDataSetChanged();
            }
        });

        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(game.setupDone)
                    return false;
                currentDragId = ((GridView) v.getParent().getParent()).getPositionForView(v);
                v.startDragAndDrop(null, new View.DragShadowBuilder(v), null, 0);
                return false;
            }
        });

        btn.setOnDragListener(this);
        return cell;
    }


    @Override
    public boolean onDrag(View v, DragEvent event){
        if(game.setupDone)
            return false;
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            case DragEvent.ACTION_DROP:
                int targetId = ((GridView) v.getParent().getParent()).getPositionForView(v);
                Ship movedShip = game.board.getShipById(currentDragId);
                ArrayList<Integer> newIds = new ArrayList<>();
                for(int id : movedShip.IDs ) {
                    id = id + targetId - currentDragId;
                    newIds.add(id);
                }
                movedShip.IDs = newIds;
                notifyDataSetChanged();
                currentDragId = -1;
                return true;
            default:
                return false;
        }
    }

}
